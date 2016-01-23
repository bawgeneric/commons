package io.kodokojo.commons.utils.serviceLocator;

/*
 * #%L
 * commons-commons
 * %%
 * Copyright (C) 2016 Kodo-kojo
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Set;

public interface ServiceLocator {

    public static final String KODOKOJO_PREFIXE = "kodokojo-";

    public static final String PROJECT_KEY = KODOKOJO_PREFIXE + "projectName";

    public static final String STACK_NAME_KEY = KODOKOJO_PREFIXE + "stackName";

    public static final String STACK_TYPY_KEY = KODOKOJO_PREFIXE + "stackType";

    public static final String COMPONENT_NAME_KEY = KODOKOJO_PREFIXE + "componentName";

    public static final String COMPONENT_TYPE_KEY = KODOKOJO_PREFIXE + "componentType";

            /**
             * Provide a Kodokojo service for a given type and name
             * @param type The service type, like registry, scm, ci.
             * @param name The name of service
             * @return <code>null</code> if no service found.
             */
    Set<Service> getService(String type, String name);

    /**
     * Provide a Kodokojo service gor a give type;
     * @param type The name of service
     * @return <code>null</code> if no service found.
     */
    Set<Service> getServiceByType(String type);

    /**
     * Provide a Kodokojo service gor a give name;
     * @param name The name of service
     * @return <code>null</code> if no service found.
     */
    Set<Service> getServiceByName(String name);

}
