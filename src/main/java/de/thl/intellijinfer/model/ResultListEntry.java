package de.thl.intellijinfer.model;

/**
 * Interface for unified access to {@link InferBug} and {@link InferBug.BugTrace} from the MainToolWindow
 */
public interface ResultListEntry {
    int getLine();
    int getColumn();
    String getFileName();
}
