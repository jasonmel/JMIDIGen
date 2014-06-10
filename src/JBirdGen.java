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

public class JBirdGen implements JMC
{
  static final int MAX_NOTE_NUM = 1000;

  // Bird
  Part bird_part;
  Phrase bird_phrase;
  JNote bird_jnote[] = new JNote[MAX_NOTE_NUM];
  int bird_num;

  Note note;
  Rest rest;

  int bar_num;
  int start_bar;
  int start_time;

  /**
   * Constructor
   */
  public JBirdGen()
  {
    bar_num    = 8;
    start_bar  = 0;
    start_time = 0;

    init();
  }

  public void init()
  {
    // Bird
    bird_part = new Part("bird part", BIRD, 2);
    bird_phrase = new Phrase();
    for (int i = 0; i < MAX_NOTE_NUM; i++)
    {
      bird_jnote[i] = new JNote();
    }
  }

  public void generate()
  {
    // Bird
    bird_num = 0;
    for (int i = 0; i < bar_num * 4; i++)
    {
      bird_jnote[i].pitch = (int)(Math.random() * 48) + 36;
      bird_jnote[i].duration = 120;
      bird_jnote[i].velocity = 60;
    }
    for (int i = 0; i < bar_num * 4; i++)
    {
      if ((int)(Math.random() * 3) == 0 ||
          i == bar_num * 4 - 1)
      {
        bird_num++;
      }
      else
      {
        bird_jnote[bird_num].duration += 120;
      }
    }
  }

  public void transform()
  {
    // Bird
    for (int i = 0; i < bird_num; i++)
    {
      note = new Note(bird_jnote[i].pitch, (double)bird_jnote[i].duration / 120, bird_jnote[i].velocity);
      bird_phrase.addNote(note);
    }
    bird_phrase.setStartTime(start_time * 4.0);
    bird_part.empty();
    bird_part.addPhrase(bird_phrase);
  }

  public void play()
  {
    bird_part.setTempo(120); // Default: 60
    Play.midi(bird_part);
  }

  public void save(String filename)
  {
    Write.midi(bird_part, filename);
  }

  public void view()
  {
    View.notate(bird_part);
  }

  /**
   * Main Function
   * @param args arguments...
   */
  public static void main(String[] args)
  {
    JBirdGen jbirdgen = new JBirdGen();

    jbirdgen.generate();
    jbirdgen.transform();
    jbirdgen.play();
    jbirdgen.save("bird.mid");
    jbirdgen.view();
  }
}

