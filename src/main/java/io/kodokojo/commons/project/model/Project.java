package io.kodokojo.commons.project.model;

/*
 * #%L
 * kodokojo-commons
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

import org.apache.commons.collections4.CollectionUtils;

import java.util.Date;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isBlank;

public class Project {

    private final String name;

    private final Date snapshotDate;

    private final Set<Stack> stacks;

    public Project(String name, Date snapshotDate, Set<Stack> stacks) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("name must be defined.");
        }
        if (snapshotDate == null) {
            throw new IllegalArgumentException("snapshotDate must be defined.");
        }
        if (CollectionUtils.isEmpty(stacks)) {
            throw new IllegalArgumentException("stacks must be defined.");
        }
        this.name = name;
        this.snapshotDate = snapshotDate;
        this.stacks = stacks;
    }

    public String getName() {
        return name;
    }

    public Date getSnapshotDate() {
        return snapshotDate;
    }

    public Set<Stack> getStacks() {
        return stacks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (!name.equals(project.name)) return false;
        if (!snapshotDate.equals(project.snapshotDate)) return false;
        return stacks.equals(project.stacks);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + snapshotDate.hashCode();
        result = 31 * result + stacks.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", snapshotDate=" + snapshotDate +
                ", stacks=" + stacks +
                '}';
    }
}
