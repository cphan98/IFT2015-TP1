import java.util.Iterator;
import java.lang.Iterable;
import java.lang.IllegalArgumentException;

/**
 * Tree is an interface for the ADT Tree.
 * It uses the interface Positon.
 *
 * Source : <a href="https://udemontreal-my.sharepoint.com/personal/francois_major_umontreal_ca/_layouts/15/onedrive.aspx?sortField=LinkFilename&isAscending=true&FolderCTID=0x012000058D0A760FE89C4BA8A2D96D83CF11CC&id=%2Fpersonal%2Ffrancois%5Fmajor%5Fumontreal%5Fca%2FDocuments%2FIFT2015%2FNotes%2FCode%2Fsrc%2Fmain%2Fjava%2Fca%2Fumontreal%2Ftrees%2FTree%2Ejava&parent=%2Fpersonal%2Ffrancois%5Fmajor%5Fumontreal%5Fca%2FDocuments%2FIFT2015%2FNotes%2FCode%2Fsrc%2Fmain%2Fjava%2Fca%2Fumontreal%2Ftrees">Tree.java</a>
 */
public interface Tree<E> extends Iterable<E> {
    Position<E> root();
    Position<E> parent(Position<E> p) throws IllegalArgumentException;
    Iterable<Position<E>> children(Position<E> p) throws IllegalArgumentException;
    int numChildren(Position<E> p) throws IllegalArgumentException;
    boolean isInternal(Position<E> p) throws IllegalArgumentException;
    boolean isExternal(Position<E> p) throws IllegalArgumentException;
    boolean isRoot(Position<E> p) throws IllegalArgumentException;
    int size();
    boolean isEmpty();
    Iterator<E> iterator();
    Iterable<Position<E>> positions();
}
