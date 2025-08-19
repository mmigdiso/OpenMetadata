/*
 *  Copyright 2024 Collate.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
import dataCollaborationPng from '../assets/img/login-screen/data-collabration/data-collbration-optimized.png';
import dataCollaborationWebp from '../assets/img/login-screen/data-collabration/data-collbration.webp';
import discoveryPng from '../assets/img/login-screen/discovery/data-discovery-optimized.png';
import discoveryWebp from '../assets/img/login-screen/discovery/data-discovery.webp';
import governancePng from '../assets/img/login-screen/governance/governce-optimized.png';
import governanceWebp from '../assets/img/login-screen/governance/governce.webp';
import observabilityPng from '../assets/img/login-screen/observability/data-observability-optimized.png';
import observabilityWebp from '../assets/img/login-screen/observability/data-observability.webp';

class LoginClassBase {
  public getLoginCarouselContent() {
    const carouselContent = [
      {
        title: 'governance',
        imagePng: governancePng,
        imageWebp: governanceWebp,
        descriptionKey: 'assess-data-reliability-with-data-profiler-lineage',
      },
      {
        title: 'data-collaboration',
        imagePng: dataCollaborationPng,
        imageWebp: dataCollaborationWebp,
        descriptionKey: 'deeply-understand-table-relations-message',
      },
      {
        title: 'data-observability',
        imagePng: observabilityPng,
        imageWebp: observabilityWebp,
        descriptionKey:
          'discover-your-data-and-unlock-the-value-of-data-assets',
      },
      {
        title: 'data-discovery',
        imagePng: discoveryPng,
        imageWebp: discoveryWebp,
        descriptionKey: 'enables-end-to-end-metadata-management',
      },
    ];

    return carouselContent;
  }
}

const loginClassBase = new LoginClassBase();

export default loginClassBase;
export { LoginClassBase };
