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

public class JBassGen implements JMC
{
  static final int MAX_NOTE_NUM = 1000;

  // Bass
  Part bass_part;
  Phrase bass_phrase;
  JNote bass_jnote[] = new JNote[MAX_NOTE_NUM];
  JNote melody_jnote[];

  Note note;
  Rest rest;

  int bar_num;
  int start_bar;
  int start_time;
  
  int beat_num;

  /**
   * Constructor
   */
  public JBassGen()
  {
    bar_num    = 8;
    start_bar  = 0;
    start_time = 0;

    init();
    
    melody_jnote = null;
    beat_num = 0;
  }
  
  public JBassGen(JNote new_melody_jnote[], int new_beat_num)
  {
    bar_num    = 8;
    start_bar  = 0;
    start_time = 0;

    init();
    
    melody_jnote = null;
    melody_jnote = new_melody_jnote;
    beat_num = new_beat_num;
  }

  public void init()
  {
    // Bass
    bass_part = new Part("bass part", TIMPANI, 1);
    bass_phrase = new Phrase();
    for (int i = 0; i < MAX_NOTE_NUM; i++)
    {
      bass_jnote[i] = new JNote();
    }
  }

  public void generate()
  {
    // Bass
    for (int i = 0; i < start_bar; i++)
    {
      bass_jnote[3 * i].pitch    = 36;
      bass_jnote[3 * i].duration = 240;
      bass_jnote[3 * i].velocity = 100;
      bass_jnote[3 * i + 1].pitch    = 43;
      bass_jnote[3 * i + 1].duration = 120;
      bass_jnote[3 * i + 1].velocity = 100;
      bass_jnote[3 * i + 2].pitch    = 43;
      bass_jnote[3 * i + 2].duration = 120;
      bass_jnote[3 * i + 2].velocity = 100;
    }
    for (int i = 0; i < bar_num; i++)
    {
      int j = i + start_bar;
      if (melody_jnote != null) bass_jnote[3 * j].pitch = 36 + melody_jnote[beat_num * i].pitch % 12;
      else bass_jnote[3 * j].pitch = 36;
      bass_jnote[3 * j].duration = 240;
      bass_jnote[3 * j].velocity = 100;
      if (melody_jnote != null) bass_jnote[3 * j + 1].pitch = 36 + melody_jnote[beat_num * (i + 1) - 1].pitch % 12 + 7;
      else bass_jnote[3 * j + 1].pitch = 43;
      bass_jnote[3 * j + 1].duration = 120;
      bass_jnote[3 * j + 1].velocity = 100;
      if (melody_jnote != null) bass_jnote[3 * j + 2].pitch = bass_jnote[3 * j + 1].pitch;
      else bass_jnote[3 * j + 2].pitch = 43;
      bass_jnote[3 * j + 2].duration = 120;
      bass_jnote[3 * j + 2].velocity = 100;
    } // end of bass for(;;)
  }

  public void transform()
  {
    // Bass
    for (int i = 0; i < (bar_num + start_bar) * 3; i++)
    {
      note = new Note(bass_jnote[i].pitch, (double)bass_jnote[i].duration / 120, bass_jnote[i].velocity);
      bass_phrase.addNote(note);
    }
    note = new Note(36, (double)240 / 120, 100);
    bass_phrase.addNote(note);
    note = new Note(43, (double)120 / 120, 100);
    bass_phrase.addNote(note);
    note = new Note(43, (double)120 / 120, 100);
    bass_phrase.addNote(note);
    note = new Note(36, (double)480 / 120, 100);
    bass_phrase.addNote(note);
    bass_phrase.setStartTime(start_time * 4.0);
    bass_part.empty();
    bass_part.addPhrase(bass_phrase);
  }

  public void play()
  {
    bass_part.setTempo(120); // Default: 60
    Play.midi(bass_part);
  }

  public void save(String filename)
  {
    Write.midi(bass_part, filename);
  }

  public void view()
  {
    View.notate(bass_part);
  }

  /**
   * Main Function
   * @param args arguments...
   */
  public static void main(String[] args)
  {
    JBassGen jbassgen = new JBassGen();

    jbassgen.generate();
    jbassgen.transform();
    jbassgen.play();
    jbassgen.save("bass.mid");
    jbassgen.view();
  }
}

