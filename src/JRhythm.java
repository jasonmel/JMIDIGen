/**
 * JRhythm.java 0.1 2006/12/15
 *
 * @author Mel Huang (a.k.a. jasonmel) <jasonmel@gmail.com>
 *
 * Copyleft (c)  2006-2006 jasonmel, All Rights Reserved.
 *
 * This software is designed for generating midi musics.
 * Rhythm per bar.
 */

import jm.JMC;
import jm.music.data.*;
import jm.midi.*;
import jm.util.*;

public class JRhythm
{
  static final int MAX_BEAT_NUM = 8;

  int beat_num;
  int beat[] = new int[MAX_BEAT_NUM];

  /**
   * Constructor
   */
  public JRhythm()
  {
    init();
  }

  public void init()
  {
    for (int i = 0; i < MAX_BEAT_NUM; i++)
    {
      beat[i] = 1;
    }
    beat_num = MAX_BEAT_NUM;
  }

  public void generate()
  {
    init();

    beat_num = 0;

    for (int i = 0; i < MAX_BEAT_NUM; i++)
    {
      if ((int)(Math.random() * 2) == 0 ||
          i == MAX_BEAT_NUM - 1)
      {
        beat_num++;
      }
      else
      {
        beat[beat_num]++;
      }
    }

    // Reverse
    /*
    int tmp;
    for (int i = 0; i < beat_num; i++)
    {
      tmp = beat[i];
      beat[i] = beat[beat_num - 1 - i];
      beat[beat_num - 1 - i] = tmp;
    }
    */

    // Not used beats
    for (int i = beat_num; i < MAX_BEAT_NUM; i++)
    {
      beat[i] = 0;
    }
  }

  public void play()
  {
    play(120);
  }

  public void play(int tempo)
  {
    Phrase tmp_phrase = new Phrase();
    Note tmp_note;

    tmp_phrase.empty();
    for (int i = 0; i < beat_num; i++)
    {
      tmp_note = new Note(60, (double)beat[i] * 60 / 120, 120);
      tmp_phrase.addNote(tmp_note);
    }

    Play.midi(tmp_phrase);
  }

  public void view()
  {
    Phrase tmp_phrase = new Phrase();
    Note tmp_note;

    tmp_phrase.empty();
    for (int i = 0; i < beat_num; i++)
    {
      tmp_note = new Note(60, (double)beat[i] * 60 / 120, 120);
      tmp_phrase.addNote(tmp_note);
    }

    View.notate(tmp_phrase);
  }

  public String toString()
  {
    String tmp = new String();

    tmp += beat_num;
    tmp += ":";
    for (int i = 0; i < MAX_BEAT_NUM; i++)
    {
      tmp += beat[i];
    }

    return tmp;
  }

  public static void main(String[] args)
  {
    JRhythm jrhythm = new JRhythm();
    jrhythm.generate();
    System.out.println(jrhythm.toString());
    jrhythm.play();
  }
}

