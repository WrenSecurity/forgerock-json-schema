/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2013 ForgeRock AS.
 */
package org.forgerock.json.resource.api;

import static org.forgerock.json.fluent.JsonValue.field;
import static org.forgerock.json.fluent.JsonValue.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.forgerock.json.fluent.JsonValue;
import org.forgerock.json.resource.ActionRequest;
import org.forgerock.json.resource.CreateRequest;
import org.forgerock.json.resource.DeleteRequest;
import org.forgerock.json.resource.NotSupportedException;
import org.forgerock.json.resource.PatchRequest;
import org.forgerock.json.resource.QueryRequest;
import org.forgerock.json.resource.QueryResultHandler;
import org.forgerock.json.resource.ReadRequest;
import org.forgerock.json.resource.RequestHandler;
import org.forgerock.json.resource.Resource;
import org.forgerock.json.resource.ResultHandler;
import org.forgerock.json.resource.ServerContext;
import org.forgerock.json.resource.UpdateRequest;

@SuppressWarnings("javadoc")
public final class Api {
    private Api() {
        // Nothing to do.
    }

    public static RequestHandler newApiDescriptorRequestHandler(final ApiDescriptor api) {
        return new RequestHandler() {

            @Override
            public void handleUpdate(final ServerContext context, final UpdateRequest request,
                    final ResultHandler<Resource> handler) {
                handler.handleError(new NotSupportedException());
            }

            @Override
            public void handleRead(final ServerContext context, final ReadRequest request,
                    final ResultHandler<Resource> handler) {
                handler.handleError(new NotSupportedException());
            }

            @Override
            public void handleQuery(final ServerContext context, final QueryRequest request,
                    final QueryResultHandler handler) {
                handler.handleError(new NotSupportedException());
            }

            @Override
            public void handlePatch(final ServerContext context, final PatchRequest request,
                    final ResultHandler<Resource> handler) {
                handler.handleError(new NotSupportedException());
            }

            @Override
            public void handleDelete(final ServerContext context, final DeleteRequest request,
                    final ResultHandler<Resource> handler) {
                handler.handleError(new NotSupportedException());
            }

            @Override
            public void handleCreate(final ServerContext context, final CreateRequest request,
                    final ResultHandler<Resource> handler) {
                handler.handleError(new NotSupportedException());
            }

            @Override
            public void handleAction(final ServerContext context, final ActionRequest request,
                    final ResultHandler<JsonValue> handler) {
                if (request.getResourceNameObject().isEmpty() && request.getAction().equals("api")) {
                    // @formatter:off
                    final JsonValue value = new JsonValue(object(
                            field("urn", String.valueOf(api.getUrn())),
                            field("description", api.getDescription()),
                            field("relations", relationsToJson(api.getRelations())),
                            field("resources", resourcesToJson(api.getResources()))
                    ));
                    // @formatter:on
                    handler.handleResult(value);
                } else {
                    handler.handleError(new NotSupportedException());
                }
            }

            private Object resourcesToJson(final Set<ResourceDescriptor> resources) {
                final List<Object> json = new ArrayList<Object>(resources.size());
                for (final ResourceDescriptor resource : resources) {
                    // @formatter:off
                    json.add(object(
                            field("urn", String.valueOf(resource.getUrn())),
                            field("description", resource.getDescription()),
                            field("parent", String.valueOf(resource.getParentUrn())),
                            field("actions", actionsToJson(resource.getActions())),
                            field("relations", relationsToJson(resource.getRelations()))
                    ));
                    // @formatter:on
                }
                return json;
            }

            private Object actionsToJson(final Set<ActionDescriptor> actions) {
                final List<Object> json = new ArrayList<Object>(actions.size());
                for (final ActionDescriptor action : actions) {
                    // @formatter:off
                    json.add(object(
                            field("name", action.getName()),
                            field("description", action.getDescription()),
                            field("parameters", action.getParameters())
                    ));
                    // @formatter:on
                }
                return json;
            }

            private Object relationsToJson(final Set<RelationDescriptor> relations) {
                final List<Object> json = new ArrayList<Object>(relations.size());
                for (final RelationDescriptor relation : relations) {
                    // @formatter:off
                    json.add(object(
                            field("name", relation.getName()),
                            field("description", relation.getDescription()),
                            field("multiplicity", relation.getMultiplicity()),
                            field("resource", String.valueOf(relation.getResource().getUrn()))
                    ));
                    // @formatter:on
                }
                return json;
            }
        };
    }
}