import java.lang.IllegalStateException;

/**
 * Position is an interface for the Position ADT
 *
 * Source : <a href="https://udemontreal-my.sharepoint.com/personal/francois_major_umontreal_ca/_layouts/15/onedrive.aspx?sortField=LinkFilename&isAscending=true&FolderCTID=0x012000058D0A760FE89C4BA8A2D96D83CF11CC&id=%2Fpersonal%2Ffrancois%5Fmajor%5Fumontreal%5Fca%2FDocuments%2FIFT2015%2FNotes%2FCode%2Fsrc%2Fmain%2Fjava%2Fca%2Fumontreal%2Fadt%2Flist%2FPosition%2Ejava&parent=%2Fpersonal%2Ffrancois%5Fmajor%5Fumontreal%5Fca%2FDocuments%2FIFT2015%2FNotes%2FCode%2Fsrc%2Fmain%2Fjava%2Fca%2Fumontreal%2Fadt%2Flist">Position.java</a>
 */
public interface Position<E> {
    // return element stored at this Position
    E getElement() throws IllegalStateException;

    // return the container of this Position
    Object getContainer();
}
