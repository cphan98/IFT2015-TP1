import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Iterable;
import java.util.List;
import java.util.Queue;

public abstract class AbstractTree<E> implements Tree<E> {
    public boolean isInternal(Position<E> p) { return numChildren(p) > 0; }
    public boolean isExternal(Position<E> p) { return numChildren(p) == 0; }
    public boolean isRoot(Position<E> p) { return p == this.root(); }
    public boolean isEmpty() { return this.size() == 0; }

    // return the number of levels separating Position p from the root (depth of p)
    public int depth(Position<E> p) {
        if (this.isRoot(p)) return 0;
        return 1 + this.depth(this.parent(p));
    }

    // return the height of the subtree rooted at Position p
    public int height(Position<E> p) {
        int h = 0;
        for (Position<E> c : this.children(p)) h = Math.max(h, 1 + this.height(c));
        return h;
    }

    // inner element iterator class
    private class ElementIterator implements Iterator<E> {
        Iterator<Position<E>> posIterator = positions().iterator();
        public boolean hasNext() { return posIterator.hasNext(); }
        public E next() { return posIterator.next().getElement(); }
        public void remove() { posIterator.remove(); }
    }

    public  Iterator<E> iterator() { return new ElementIterator(); }

    // return a position iterable of the list
    public Iterable<Position<E>> positions() { return preorder(); }

    // add the position of the subtree rooted at p to the given snapshot
    private void preorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        // for preorder, add position p before exploring subtrees
        snapshot.add(p);
        for (Position<E> c : children(p)) preorderSubtree(c, snapshot);
    }

    // return an iterable collection of positions of the tree, reported in preorder
    public  Iterable<Position<E>> preorder() {
        List<Position<E>> snapshot = new ArrayList<>();

        // fill the snapshot recursively
        if (!this.isEmpty()) preorderSubtree(this.root(), snapshot);

        return snapshot;
    }

    // add positions of the subtree rooted at p to the given snapshot
    private void postorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        for (Position<E> c : children(p)) postorderSubtree(c, snapshot);

        // for postorder, add position p after exploring subtrees
        snapshot.add(p);
    }

    // return an iterable collection of positions of the tree, reported in postorder
    public Iterable<Position<E>> postorder() {
        List<Position<E>> snapshot = new ArrayList<>();

        // fill the snapshot recursively
        if (!this.isEmpty()) postorderSubtree(this.root(), snapshot);

        return snapshot;
    }

    public Iterable<Position<E>> breadthFirst() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!this.isEmpty()) {
            Queue<Position<E>> fringe = new LinkedQueue<>();

            // start with root
            fringe.enqueue(this.root());

            while (!fringe.isEmpty()) {
                // remove from front of the queue
                Position<E> p = fringe.dequeue();

                // report this position
                snapshot.add(p);

                // add children to back of queue
                for (Position<E> c : children(p)) fringe.enqueue(c);
            }
        }
        return snapshot;
    }
}
