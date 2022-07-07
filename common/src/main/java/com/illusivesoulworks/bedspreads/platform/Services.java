/*
 * Copyright (C) 2018-2022 Illusive Soulworks
 *
 * Bedspreads is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Bedspreads is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Bedspreads.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.bedspreads.platform;

import com.illusivesoulworks.bedspreads.BedspreadsConstants;
import com.illusivesoulworks.bedspreads.platform.services.IPlatformRegistry;
import java.util.ServiceLoader;

public class Services {

  public static final IPlatformRegistry REGISTRY = load(IPlatformRegistry.class);

  public static <T> T load(Class<T> clazz) {

    final T loadedService = ServiceLoader.load(clazz)
        .findFirst()
        .orElseThrow(
            () -> new NullPointerException("Failed to load service for " + clazz.getName()));
    BedspreadsConstants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
    return loadedService;
  }
}
