/*
 * Copyright (c) 2013 GraphAware
 *
 * This file is part of GraphAware.
 *
 * GraphAware is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.graphaware.tx.event.improved.propertycontainer.snapshot;

import com.graphaware.common.util.IterableUtils;
import com.graphaware.common.wrapper.NodeWrapper;
import com.graphaware.tx.event.improved.data.PropertyContainerTransactionData;
import com.graphaware.tx.event.improved.data.TransactionDataContainer;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.IterableWrapper;

/**
 * A {@link PropertyContainerSnapshot} representing a {@link org.neo4j.graphdb.Node}.
 */
public class NodeSnapshot extends PropertyContainerSnapshot<Node> implements Node, NodeWrapper {
    private static final Logger LOG = Logger.getLogger(NodeSnapshot.class);

    /**
     * Construct a snapshot.
     *
     * @param wrapped                  node.
     * @param transactionDataContainer transaction data container.
     */
    public NodeSnapshot(Node wrapped, TransactionDataContainer transactionDataContainer) {
        super(wrapped, transactionDataContainer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PropertyContainerTransactionData<Node> transactionData() {
        return transactionDataContainer.getNodeTransactionData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Node self() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete() {
        if (transactionDataContainer.getNodeTransactionData().hasBeenDeleted(wrapped)) {
            LOG.warn("Node " + getId() + " has already been deleted in this transaction.");
        } else {
            super.delete();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship createRelationshipTo(Node otherNode, RelationshipType type) {
        checkCanBeMutated();
        return super.createRelationshipTo(otherNode, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Relationship wrapRelationship(Relationship relationship) {
        return new RelationshipSnapshot(relationship, transactionDataContainer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Iterable<Relationship> wrapRelationships(Iterable<Relationship> relationships, Direction direction, RelationshipType... relationshipTypes) {
        return new RelationshipSnapshotIterator(this, relationships, transactionDataContainer, direction, relationshipTypes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLabel(Label label) {
        return IterableUtils.contains(getLabels(), label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Label> getLabels() {
        return new LabelSnapshotIterator(this, super.getLabels(), transactionDataContainer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLabel(Label label) {
        checkCanBeMutated();
        super.addLabel(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLabel(Label label) {
        checkCanBeMutated();
        super.removeLabel(label);
    }

    @Override
    public int getDegree() {
        return super.getDegree();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int getDegree(RelationshipType type) {
        return super.getDegree(type);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int getDegree(Direction direction) {
        return super.getDegree(direction);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int getDegree(RelationshipType type, Direction direction) {
        return super.getDegree(type, direction);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
