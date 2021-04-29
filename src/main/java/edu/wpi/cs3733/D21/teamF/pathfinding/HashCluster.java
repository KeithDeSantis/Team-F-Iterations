package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.util.HashMap;
import java.util.Objects;

public class HashCluster<Payload> {
    private final HashMap<Payload, HashChain.HashNode<Payload>> links;
    private final HashMap<Payload, HashChain<Payload>> chains;

    /**
     * Creates a new HashCluster
     * @author Tony Vuolo (bdane)
     */
    public HashCluster() {
        this.links = new HashMap<>();
        this.chains = new HashMap<>();
    }

    /**
     * Attempts to add a new Payload to this HashCluster
     * @param addend the addend Payload
     * @return true if the Payload was successfully added to this HashCluster, else false
     */
    public boolean add(Payload addend) {
        if(this.links.containsKey(addend)) {
            return false;
        }
        HashChain.HashNode<Payload> node = new HashChain.HashNode<>(addend);
        this.links.put(addend, node);
        this.chains.put(addend, new HashChain<>(node, node));
        return true;
    }

    /**
     * Attempts to join two Payloads in this HashCluster
     * @param a the first Payload
     * @param b the second Payload
     * @author Tony Vuolo (bdane)
     */
    public void join(Payload a, Payload b) {
        HashChain<Payload> aChain = this.chains.get(a), bChain = this.chains.get(b);
        if(!(aChain == null || bChain == null || aChain.equals(bChain))) {
            HashChain.HashNode<Payload> aNode = this.links.get(a), bNode = this.links.get(b);
            this.chains.replace(bChain.getOtherEnd(b).payload, aChain);
            if(aNode.node2 == null) {
                aNode.node2 = bNode;
                if(aNode.node1 != null) {
                    this.chains.replace(a, null);
                }
            } else {
                aNode.node1 = bNode;
                this.chains.replace(a, null);
            }
            if(bNode.node2 == null) {
                bNode.node2 = aNode;
                if(bNode.node1 != null) {
                    this.chains.replace(b, null);
                }
            } else {
                bNode.node1 = aNode;
                this.chains.replace(b, null);
            }
            aChain.end1 = (aChain.end1 == null) ? aChain.end2 : aChain.end1;
            aChain.end2 = bChain.getOtherEnd(b);
        }
    }

    /**
     * Attempts to remove a Payload from this HashCluster
     * @param a the target Payload
     * @return true if an element was successfully removed from the HashCluster, else false
     * @author Tony Vuolo (bdane)
     */
    public boolean remove(Payload a) {
        HashChain.HashNode<Payload> node = this.links.get(a);
        if(node == null) {
            return false;
        }
        HashChain<Payload> chain = this.chains.get(a);
        if(chain == null) {
            HashChain.HashNode<Payload> node1 = node.node1, node2 = node.node2;
            if(node2.node2 != null && node2.node2.equals(node)) {
                node2.node2 = node1;
            } else {
                node2.node1 = node1;
            }
            if(node1.node2 != null && node1.node2.equals(node)) {
                node1.node2 = node2;
            } else {
                node1.node1 = node2;
            }
        } else if(node.node1 != null || node.node2 != null) {
            HashChain.HashNode<Payload> nextNode = (node.node1 == null) ? node.node2 : node.node1;
            if(nextNode.node1 != null && nextNode.node1.equals(node)) {
                nextNode.node1 = null;
            } else {
                nextNode.node2 = null;
            }
            this.chains.replace(nextNode.payload, chain);
            if(chain.end1.equals(node)) {
                chain.end1 = nextNode;
            } else {
                chain.end2 = nextNode;
            }
        }
        this.links.remove(a);
        this.chains.remove(a);
        return true;
    }

    /**
     * Determines whether a given Payload in this HashCluster is isolated
     * @param p the target Payload
     * @return true if the Payload exists in this HashCluster and has no neighbors, else false
     */
    public boolean isIsolated(Payload p) {
        HashChain.HashNode<Payload> node = this.links.get(p);
        return node != null && node.node1 == null && node.node2 == null;
    }

    /**
     * Prints this HashCluster
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(Payload key : this.links.keySet()) {
            HashChain.HashNode<Payload> value = this.links.get(key);
            builder.append(value.node1).append(" - ").append(value.payload).append(" - ").append(value.node2).append("\n");
        }
        return builder.toString();
    }

    /**
     * Keeps a sequential but unordered list of HashNodes
     * @param <Payload> the Payload contained in the chain
     * @author Tony Vuolo (bdane)
     */
    private static class HashChain<Payload> {
        private HashNode<Payload> end1, end2;

        /**
         * Creates a new HashChain
         * @param end1 one end of the HashChain
         * @param end2 the other end of the HashChain
         * @author Tony Vuolo (bdane)
         */
        private HashChain(HashNode<Payload> end1, HashNode<Payload> end2) {
            this.end1 = end1;
            this.end2 = end2;
        }

        /**
         * Gets the other end of this HashChain
         * @param p the comparator Payload
         * @return null if neither end is a HashNode containing p, else the end not containing p
         * @author Tony Vuolo (bdane)
         */
        private HashNode<Payload> getOtherEnd(Payload p) {
            return this.end1.equals(p) ? this.end2 : this.end1;
        }

        /**
         * Determines whether this HashChain is equal to another HashChain
         * @param chain the comparator Chain
         * @return true if the end nodes are equal, else false
         * @author Tony Vuolo (bdane)
         */
        private boolean equals(HashChain<Payload> chain) {
            return (chain.end1.equals(this.end1) && chain.end2.equals(this.end2))
                    || (chain.end2.equals(this.end1) && chain.end1.equals(this.end2));
        }

        /**
         * Holds a Payload object in a linked node
         * @param <Payload> the type parameter
         * @author Tony Vuolo (bdane)
         */
        private static class HashNode<Payload> {
            HashNode<Payload> node1, node2;
            Payload payload;

            /**
             * Creates a new HashNode
             * @param payload the payload of this HashNode
             * @author Tony Vuolo (bdane)
             */
            private HashNode(Payload payload) {
                this.payload = payload;
            }

            /**
             * Gets the other neighbor
             * @param p the comparator Payload
             * @return null if neither neighbor is a HashNode containing p, else the neighbor not containing p
             * @author Tony Vuolo (bdane)
             */
            private HashNode<Payload> getOtherNeighbor(Payload p) {
                if(this.node1 != null && this.node1.equals(p)) {
                    return this.node2;
                } else if(this.node2 != null && this.node2.equals(p)) {
                    return this.node1;
                } else {
                    return null;
                }
            }

            /**
             * Determines whether this HashNode is equal to another HashNode
             * @param node the comparator HashNode
             * @return true if the Payloads are equal, else false
             * @author Tony Vuolo (bdane)
             */
            private boolean equals(HashNode<Payload> node) {
                if(this.payload == null || node.payload == null) {
                    return this.payload == node.payload;
                }
                return this.payload.equals(node.payload);
            }

            /**
             * Converts this HashNode to a printable format
             * @return this HashNode as a String
             * @author Tony Vuolo (bdane)
             */
            public String toString() {
                return "" + this.payload;
            }
        }
    }
}
