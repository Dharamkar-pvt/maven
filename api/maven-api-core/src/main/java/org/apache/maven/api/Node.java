package org.apache.maven.api;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.api.annotations.Experimental;
import org.apache.maven.api.annotations.Immutable;
import org.apache.maven.api.annotations.Nonnull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Represents a dependency node within a Maven project's dependency collector.
 *
 * @since 4.0
 * @see org.apache.maven.api.services.DependencyCollectorResult#getRoot()
 */
@Experimental
@Immutable
public interface Node
{

    /**
     * @return dependency for this node
     */
    Dependency getDependency();

    /**
     * Gets the child nodes of this node.
     *
     * @return the child nodes of this node, never {@code null}
     */
    @Nonnull
    List<Node> getChildren();

    /**
     * @return repositories of this node
     */
    @Nonnull
    List<RemoteRepository> getRemoteRepositories();

    /**
     * The repository where this artifact has been downloaded from.
     */
    @Nonnull
    Optional<RemoteRepository> getRepository();

    /**
     * Traverses this node and potentially its children using the specified visitor.
     *
     * @param visitor The visitor to call back, must not be {@code null}.
     * @return {@code true} to visit siblings nodes of this node as well, {@code false} to skip siblings.
     */
    boolean accept( @Nonnull NodeVisitor visitor );

    /**
     * Returns a new tree starting at this node, filtering the children.
     * Note that this node will not be filtered and only the children
     * and its descendant will be checked.
     *
     * @param filter the filter to apply
     * @return a new filtered graph
     */
    Node filter( Predicate<Node> filter );

    /**
     * Returns a string representation of this dependency node.
     *
     * @return the string representation
     */
    String asString();

    /**
     * Obtain a Stream containing this node and all its descendant.
     *
     * @return a stream containing this node and its descendant
     */
    @Nonnull
    default Stream<Node> stream()
    {
        return Stream.concat( Stream.of( this ), getChildren().stream().flatMap( Node::stream ) );
    }

}
