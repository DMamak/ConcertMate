package com.example.concertmate.tests;

import com.example.concertmate.Models.Notes;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class NotesTest extends TestCase {
   private Notes notes;

    protected void setUp() throws Exception {
        super.setUp();
        notes = new Notes();
    }

    public void testNotes() {
        notes.toString();
    }

    public void testGetNote() {
        String expected = String.valueOf(Math.random());
        notes.setNotes(expected);
        String actual = notes.getNotes();
        Assert.assertEquals(expected, actual);
    }
}
