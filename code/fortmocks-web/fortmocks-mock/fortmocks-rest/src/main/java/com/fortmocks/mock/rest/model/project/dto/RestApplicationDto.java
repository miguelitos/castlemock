/*
 * Copyright 2015 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortmocks.mock.rest.model.project.dto;

import org.dozer.Mapping;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public class RestApplicationDto {

    @Mapping("id")
    private Long id;

    @Mapping("name")
    private String name;

    @Mapping("restResources")
    private List<RestResourceDto> restResources = new LinkedList<RestResourceDto>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RestResourceDto> getRestResources() {
        return restResources;
    }

    public void setRestResources(List<RestResourceDto> restResources) {
        this.restResources = restResources;
    }
}