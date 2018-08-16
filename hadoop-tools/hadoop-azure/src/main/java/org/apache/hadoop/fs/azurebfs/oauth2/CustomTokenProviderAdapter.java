/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.fs.azurebfs.oauth2;

import com.google.common.base.Preconditions;
import org.apache.hadoop.fs.azurebfs.contracts.services.LoggingService;

import java.io.IOException;

/**
 * Provides tokens based on custom implementation, following the Adapter Design
 * Pattern.
 */
public final class CustomTokenProviderAdapter extends AccessTokenProvider {

  private CustomTokenProviderAdaptee adaptee;
  private final LoggingService loggingService;

  /**
   * Constructs a token provider based on the custom token provider.
   *
   * @param loggingService the logging service
   * @param adaptee the custom token provider
   */
  public CustomTokenProviderAdapter(final LoggingService loggingService, CustomTokenProviderAdaptee adaptee) {
    super(loggingService);

    Preconditions.checkNotNull(loggingService, "loggingService");
    Preconditions.checkNotNull(adaptee, "adaptee");

    this.loggingService = loggingService.get(CustomTokenProviderAdapter.class);

    this.adaptee = adaptee;
  }

  protected AzureADToken refreshToken() throws IOException {

    this.loggingService.debug("AADToken: refreshing custom based token");

    AzureADToken azureADToken = new AzureADToken();
    azureADToken.setAccessToken(adaptee.getAccessToken());
    azureADToken.setExpiry(adaptee.getExpiryTime());

    return azureADToken;
  }
}
