package de.thl.intellijinfer.model;

import java.io.Serializable;

public class InferVersion implements Comparable<InferVersion>, Serializable {
    private int major;
    private int minor;
    private int patch;

    public InferVersion() {}

    public InferVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public String toString() {
        return "v" + major + "." + minor + "." + patch;
    }

    public int compareTo(InferVersion iv) {
        if (this.major != iv.getMajor()) {
            return Integer.compare(this.major,iv.getMajor());
        }
        if (this.minor != iv.getMinor()) {
            return Integer.compare(this.minor, iv.getMinor());
        }
        if (this.patch != iv.getPatch()) {
            return Integer.compare(this.patch, iv.getPatch());
        }
        return 0;
    }

    public int getMajor() {
        return major;
    }
    public void setMajor(int major) {
        this.major = major;
    }
    public int getMinor() {
        return minor;
    }
    public void setMinor(int minor) {
        this.minor = minor;
    }
    public int getPatch() {
        return patch;
    }
    public void setPatch(int patch) {
        this.patch = patch;
    }
}
