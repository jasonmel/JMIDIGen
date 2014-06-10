/**
 * MIDIGen.java  0.1 2006/12/15
 *
 * @author Mel Huang (a.k.a. jasonmel) <jasonmel@gmail.com>
 *
 * Copyleft (c)  2006-2006 jasonmel, All Rights Reserved.
 *
 * This software is designed for generating midi musics.
 */

import jm.JMC;
import jm.music.data.*;
import jm.midi.*;
import jm.util.*;

public class JDrumGen implements JMC
{
  static final int MAX_NOTE_NUM = 1000;

  // Drum
  Part drum_part;
  Phrase drum_phrase0;
  Phrase drum_phrase1;
  Phrase drum_phrase2;

  Note note;
  Rest rest;

  int bar_num;
  int start_bar;
  int start_time;

  /**
   * Constructor
   */
  public JDrumGen()
  {
    bar_num    = 8;
    start_bar  = 0;
    start_time = 0;

    init();
  }

  public void init()
  {
    // Drum
    drum_part = new Part("drum part", 0, 9);
    drum_phrase0 = new Phrase(0.0);
    drum_phrase1 = new Phrase(0.0);
    drum_phrase2 = new Phrase(0.0);
  }

  public void generate()
  {
  }

  public void transform()
  {
    // Drum
    // hats
    for (int i = 0; i < (bar_num + start_bar) * 4; i++)
    {
      note = new Note(42, (double)120 / 120, 60);
      drum_phrase0.addNote(note);
    }
    // bass and snare
    for (int i = 0; i < (bar_num + start_bar); i++)
    {
      note = new Note(36, (double)120 / 120, 60);
      drum_phrase1.addNote(note);
      note = new Note(REST, (double)60 / 120, 60);
      drum_phrase1.addNote(note);
      note = new Note(36, (double)180 / 120, 60);
      drum_phrase1.addNote(note);
      note = new Note(REST, (double)120 / 120, 60);
      drum_phrase1.addNote(note);

      note = new Note(REST, (double)120 / 120, 60);
      drum_phrase2.addNote(note);
      note = new Note(38, (double)60 / 120, 60);
      drum_phrase2.addNote(note);
      note = new Note(REST, (double)180 / 120, 60);
      drum_phrase2.addNote(note);
      note = new Note(38, (double)120 / 120, 60);
      drum_phrase2.addNote(note);
    }
    drum_part.empty();
    drum_part.addPhrase(drum_phrase0);
    drum_part.addPhrase(drum_phrase1);
    drum_part.addPhrase(drum_phrase2);
  }

  public void play()
  {
    drum_part.setTempo(120); // Default: 60
    Play.midi(drum_part);
  }

  public void save(String filename)
  {
    Write.midi(drum_part, filename);
  }

  public void view()
  {
    View.notate(drum_part);
  }

  /**
   * Main Function
   * @param args arguments...
   */
  public static void main(String[] args)
  {
    JDrumGen jdrumgen = new JDrumGen();

    jdrumgen.generate();
    jdrumgen.transform();
    jdrumgen.play();
    jdrumgen.save("drum.mid");
    jdrumgen.view();
  }
}

