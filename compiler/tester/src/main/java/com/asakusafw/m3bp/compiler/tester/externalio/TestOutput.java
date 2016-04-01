/**
 * Copyright 2011-2016 Asakusa Framework Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.asakusafw.m3bp.compiler.tester.externalio;

import com.asakusafw.lang.compiler.model.description.Descriptions;
import com.asakusafw.lang.compiler.model.description.ImmediateDescription;
import com.asakusafw.lang.compiler.model.info.ExternalOutputInfo;
import com.asakusafw.vocabulary.external.ExporterDescription;

/**
 * An implementation of {@link ExporterDescription} for testing.
 * {@link TestExternalPortProcessor} can process this description.
 */
public abstract class TestOutput implements ExporterDescription {

    /**
     * Creates a new instance.
     * @param id the output ID
     * @param dataType the data type
     * @return the created instance
     */
    public static TestOutput of(String id, Class<?> dataType) {
        return new Basic(id, dataType);
    }

    /**
     * Returns the ID.
     * @return the ID
     */
    public abstract String getId();

    /**
     * Returns an {@link ExternalOutputInfo} object for this.
     * @return {@link ExternalOutputInfo} object
     */
    public ExternalOutputInfo toInfo() {
        return new ExternalOutputInfo.Basic(
                Descriptions.classOf(getClass()),
                TestExternalPortProcessor.MODULE_NAME,
                Descriptions.classOf(getModelType()),
                new ImmediateDescription(Descriptions.typeOf(String.class), getId()));
    }

    /**
     * A basic implementation of {@link TestOutput}.
     */
    public static class Basic extends TestOutput {

        private final String id;

        private final Class<?> aClass;

        /**
         * Creates a new instance.
         * @param id the output ID
         * @param aClass the data type
         */
        public Basic(String id, Class<?> aClass) {
            this.id = id;
            this.aClass = aClass;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public Class<?> getModelType() {
            return aClass;
        }
    }
}