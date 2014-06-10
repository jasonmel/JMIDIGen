/**
 * JMelodyGen.java  0.1 2006/12/15
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

public class JMelodyGen implements JMC
{
  static final int MAX_NOTE_NUM = 1000;

  static final int VEL_LOW  = 0;
  static final int VEL_UP   = 1;
  static final int VEL_HIGH = 2;
  static final int VEL_DOWN = 3;

  Part melody_part;
  Phrase melody_phrase;
  JNote melody_jnote[] = new JNote[MAX_NOTE_NUM];
  JRhythm jrhythm;

  Note note;
  Rest rest;

  int bar_num;
  int start_bar;
  int start_time;

  /**
   * Constructor
   */
  public JMelodyGen()
  {
    bar_num    = 8;
    start_bar  = 0;
    start_time = 0;

    init();

    jrhythm = new JRhythm();
    do { jrhythm.generate(); } while (jrhythm.beat_num <= 1);
  }
  
  public JMelodyGen(JRhythm new_jrhythm)
  {
    bar_num = 8;

    init();

    jrhythm = new_jrhythm;
  }

  public void init()
  {
    melody_part = new Part("melody part", PIANO, 0);
    melody_phrase = new Phrase();
    for (int i = 0; i < MAX_NOTE_NUM; i++)
    {
      melody_jnote[i] = new JNote();
    }
  }

  public void generate()
  {
    int beat_num  = jrhythm.beat_num;
    int vel_state = (int)(Math.random() * 4);
    int pit_state;
    int current, next;

    // Melody
    melody_jnote[0].note_on = 0;
    melody_jnote[0].pitch = 60; // C5
    for (int i = 0; i < bar_num; i++)
    {
      vel_state = velState(vel_state);
      for (int j = 0; j < beat_num; j++)
      {
        current = i * beat_num + j;
        next = current + 1;

        // Pitch
        //pit_state = (int)(Math.random() * 4);
        pit_state = (int)(Math.random() * 3);
        switch (pit_state)
        {
        case 0:
          melody_jnote[next].pitch = (int)(Math.random() * 36) + 48; // C3-C7
          break;
        case 1:
          melody_jnote[next].pitch = melody_jnote[current].pitch + 1;
          break;
        case 2:
          melody_jnote[next].pitch = melody_jnote[current].pitch - 1;
          break;
        //case 3:
        //  melody_jnote[next].pitch = melody_jnote[current].pitch;
        //  break;
        }
        // to C Major
        melody_jnote[next].pitch = toCMajor(pit_state, melody_jnote[next].pitch);

        // Velocity
        switch (vel_state)
        {
        case VEL_LOW:
          melody_jnote[current].velocity = 60;
          break;
        case VEL_UP:
          melody_jnote[current].velocity = 60 + 60 * j / (beat_num - 1);
          break;
        case VEL_HIGH:
          melody_jnote[current].velocity = 120;
          break;
        case VEL_DOWN:
          melody_jnote[current].velocity = 120 - 60 * j / (beat_num - 1);
          break;
        }

        // Duration, rest and note_on
        melody_jnote[current].duration = 60 * jrhythm.beat[j]; // whole: 480
        melody_jnote[current].rest = 0;
        melody_jnote[next].note_on = melody_jnote[current].note_on + melody_jnote[current].duration + melody_jnote[current].rest;
      }
    } // end of melody for(;;)
  }

  public int velState(int current)
  {
    int next = current;

    if (current == VEL_LOW || current == VEL_DOWN)
    {
      if ((int)(Math.random() * 3) == 0) { next = VEL_UP; }
      else { next = VEL_LOW; }
    }
    else if (current == VEL_UP || current == VEL_HIGH)
    {
      if ((int)(Math.random() * 3) == 0) { next = VEL_DOWN; }
      else { next = VEL_HIGH; }
    }

    return next;
  }

  public int toCMajor(int pit_state, int pitch)
  {
    // Become C Major
    if (((pitch % 12 <= 4) &&
         (pitch % 2 == 1)) ||
        ((pitch % 12 >= 5) &&
         (pitch % 2 == 0)))
    {
      if (pit_state == 2) { pitch--; }
      else { pitch++; }
    }

    return pitch;
  }

  public void transform()
  {
    // Melody
    for (int i = 0; i < bar_num * jrhythm.beat_num; i++)
    {
      note = new Note(melody_jnote[i].pitch, (double)melody_jnote[i].duration / 120, melody_jnote[i].velocity);
      melody_phrase.addNote(note);

      if (melody_jnote[i].rest > 0)
      {
        rest = new Rest((double)melody_jnote[i].rest / 240);
        melody_phrase.addRest(rest);
      }
    }
    note = new Note(60, (double)480 / 120, 120);
    melody_phrase.addNote(note);
    melody_phrase.setStartTime(start_time * 4.0);
    melody_part.empty();
    melody_part.addPhrase(melody_phrase);
  }

  public void play()
  {
    melody_part.setTempo(120); // Default: 60
    Play.midi(melody_part);
  }

  public void save(String filename)
  {
    Write.midi(melody_part, filename);
  }

  public void view()
  {
    View.notate(melody_part);
  }

  /**
   * Main Function
   * @param args arguments...
   */
  public static void main(String[] args)
  {
    JMelodyGen jmelodygen = new JMelodyGen();

    jmelodygen.generate();
    jmelodygen.transform();
    jmelodygen.play();
    jmelodygen.save("melody.mid");
    jmelodygen.view();
  }
}

