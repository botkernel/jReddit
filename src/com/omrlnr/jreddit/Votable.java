package com.omrlnr.jreddit;

/**
 *
 * This interface represents Things which can be voted on.
 * See
 * https://github.com/reddit/reddit/wiki/JSON#votable-implementation
 *
 * @author <a href="https://github.com/jasonsimpson">Jason Simpson</a>
 *
 */
public interface Votable {

    public String getName();    

    public long getUpVotes();
    public long getDownVotes();
    public Boolean getLikes();

}

