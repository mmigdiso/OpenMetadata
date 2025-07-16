package org.openmetadata.service.governance.workflows;

import java.util.UUID;

import jakarta.json.JsonPatch;
import lombok.extern.slf4j.Slf4j;
import org.openmetadata.schema.entity.data.GlossaryTerm;
import org.openmetadata.schema.type.Include;
import org.openmetadata.schema.type.change.ChangeSource;
import org.openmetadata.schema.utils.JsonUtils;
import org.openmetadata.service.Entity;
import org.openmetadata.service.jdbi3.GlossaryTermRepository;

/**
 * Utility methods for workflow-related entity operations, such as auto-approving glossary terms.
 */
@Slf4j
public class WorkflowUtil {

  /**
   * Set glossary original status to APPROVED in the DB, used for auto-approval by workflow triggers.
   * Accepts the original in memory, avoids extra DB fetches.
   */
  public static boolean setGlossaryTermStatusApproved(GlossaryTerm original, String updatedBy) {
    try {
      GlossaryTerm updated = JsonUtils.deepCopy(original, GlossaryTerm.class);
      if (original.getStatus() != GlossaryTerm.Status.APPROVED) {
        original.setStatus(GlossaryTerm.Status.APPROVED);
        original.setUpdatedBy(updatedBy);
        JsonPatch patch = JsonUtils.getJsonPatch(
            JsonUtils.pojoToJson(updated),
            JsonUtils.pojoToJson(original)
        );
        GlossaryTermRepository glossaryTermRepository =
            (GlossaryTermRepository) Entity.getEntityRepository(Entity.GLOSSARY_TERM);
        glossaryTermRepository.patch(null, original.getId(), updatedBy, patch, null);

        LOG.info("Auto-approved glossary term '{}' by reviewer '{}'", original.getFullyQualifiedName(), updatedBy);
        return true;
      }
    } catch (Exception e) {
      LOG.error("Failed to auto-approve glossary term {}: {}, Falling back to workflow", original.getId(), e.getMessage(), e);
      return false;
    }
    return false;
  }

  public static boolean shouldTriggerGlossaryTermWorkflow(UUID entityId, String updatedBy) {
    GlossaryTerm term = Entity.getEntity(
        Entity.getEntityReferenceById(Entity.GLOSSARY_TERM, entityId, Include.NON_DELETED),
        "status,reviewers", Include.NON_DELETED);
    // If already approved, skip workflow
    if (term.getStatus() == GlossaryTerm.Status.APPROVED) {
      return false;
    }
    // If updatedBy is missing, log and trigger workflow for safety
    if (updatedBy == null || updatedBy.isEmpty()) {
      LOG.warn("UpdatedBy is missing for glossary term event: {}", entityId);
      return true;
    }
    // Check if updater is a reviewer (direct or inherited)
    boolean isReviewer = term.getReviewers() != null &&
        term.getReviewers().stream().anyMatch(ref ->
            ref.getName() != null && ref.getName().equals(updatedBy)
        );
    if (isReviewer) {
      // Auto-approve in DB, if there is any error, fall back to workflow
      return !setGlossaryTermStatusApproved(term, updatedBy);
    }
    return true;
  }
} 