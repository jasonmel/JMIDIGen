/**
 * MIDIGen.java  0.1 2006/12/15
 *
 * @author Mel Huang (a.k.a. jasonmel) <jasonmel@gmail.com>
 *
 * Copyleft (c)  2006-2006 jasonmel, All Rights Reserved.
 *
 * This software is designed for generating midi musics.
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

import jm.JMC;
import jm.music.data.*;
import jm.midi.*;
import jm.util.*;

public class JMIDIGen extends Frame
{
  Score score = new Score("Jasonmel Score");

  JRhythm jrhythm = new JRhythm();
  JMelodyGen jmelodygen = new JMelodyGen();
  JBassGen jbassgen = new JBassGen();
  JBirdGen jbirdgen = new JBirdGen();
  JDrumGen jdrumgen = new JDrumGen();

  // Menu
  MenuBar menubar = new MenuBar();
  Menu menu1 = new Menu( "File" );
  MenuItem menuitem_Exit = new MenuItem( "Exit" );
  Menu menu2 = new Menu( "Help" );
  MenuItem menuitem_About = new MenuItem( "About" );

  // Panel
  Panel panel1 = new Panel();
  Panel panel2 = new Panel();
  Panel panel3 = new Panel();
  Panel panel4 = new Panel();
  Panel panel5 = new Panel();
  Panel panel_CENTER = new Panel();

  // Components in panel1
  Label label1_Rhythm = new Label( "Rhythm: " );
  Button btn1_RhyGen = new Button( "Generate" );
  Button btn1_RhyApply = new Button( "Apply" );
  
  // Components in panel2
  Label label2_Melody = new Label( "Number of melody bars: " );
  TextField txt2_BarNum = new TextField( "8", 5 );

  // Components in panel3
  Label label3_Tempo = new Label( "Tempo (default 60): " );
  TextField txt3_Tempo = new TextField( "60", 5 );

  // Components in panel4
  Checkbox chkbox4_Melody = new Checkbox( "Melody" );
  Checkbox chkbox4_Bass = new Checkbox( "Bass" );
  Checkbox chkbox4_Bird = new Checkbox( "Bird" );
  Checkbox chkbox4_Drum = new Checkbox( "Drum" );

  // Components in panel5
  Button btn5_Gen = new Button( "Generate" );
  Button btn5_View = new Button( "View" );
  Button btn5_Play = new Button( "Play" );
  Button btn5_Save = new Button( "Save" );

  JEvent jevent = new JEvent();

  /**
   * Constructor
   */
  public JMIDIGen()
  {
    // Setup menu
    menubar.add( menu1 );
    menubar.add( menu2 );
    menu1.add( menuitem_Exit );
    menu2.add( menuitem_About );
    this.setMenuBar( menubar );
    menuitem_Exit.addActionListener( jevent );
    menuitem_About.addActionListener( jevent );

    // Setup frame
    this.setSize( 300, 250 );
    this.setTitle( "Jasonmel MIDI Music Generator" );

    // Setup panel1
    panel1.setLayout( new FlowLayout() );
    panel1.add( label1_Rhythm );
    panel1.add( btn1_RhyGen );
    //panel1.add( btn1_RhyApply );
    btn1_RhyGen.addActionListener( jevent );
    btn1_RhyApply.addActionListener( jevent );

    // Setup panel2
    panel2.setLayout( new FlowLayout() );
    panel2.add( label2_Melody );
    panel2.add( txt2_BarNum );

    // Setup panel3
    panel3.setLayout( new FlowLayout() );
    panel3.add( label3_Tempo );
    panel3.add( txt3_Tempo );

    // Setup panel4
    panel4.setLayout( new FlowLayout() );
    panel4.add( chkbox4_Melody );
    panel4.add( chkbox4_Bass );
    panel4.add( chkbox4_Bird );
    panel4.add( chkbox4_Drum );

    // Setup panel5
    panel5.setLayout( new FlowLayout() );
    panel5.add( btn5_Gen );
    panel5.add( btn5_View );
    panel5.add( btn5_Play );
    panel5.add( btn5_Save );
    btn5_Gen.addActionListener( jevent );
    btn5_View.addActionListener( jevent );
    btn5_Play.addActionListener( jevent );
    btn5_Save.addActionListener( jevent );
    btn5_View.setEnabled( false );
    btn5_Play.setEnabled( false );
    btn5_Save.setEnabled( false );

    panel_CENTER.add( panel2, BorderLayout.NORTH );
    panel_CENTER.add( panel3, BorderLayout.CENTER );
    panel_CENTER.add( panel4, BorderLayout.SOUTH );

    this.add( panel1, BorderLayout.NORTH );
    this.add( panel_CENTER, BorderLayout.CENTER );
    this.add( panel5, BorderLayout.SOUTH );

    this.setVisible( true );
  }

  /**
   * Event Listener
   */
  class JEvent implements ActionListener
  {
    public void actionPerformed( ActionEvent evt )
    {
      if (evt.getSource() == menuitem_Exit)
      {
        System.exit(0);
      }
      else if (evt.getSource() == menuitem_About)
      {
      }
      else if (evt.getSource() == btn1_RhyGen)
      {
        do { jrhythm.generate(); } while (jrhythm.beat_num <= 1);
        //jrhythm.play( Integer.valueOf( txt3_Tempo.getText() ).intValue() );
        jrhythm.view();
        //System.out.println( jrhythm );
      }
      else if (evt.getSource() == btn1_RhyApply)
      {
        jmelodygen = new JMelodyGen( jrhythm );
      }
      else if (evt.getSource() == btn5_Gen)
      {
        score.empty();

        // Melody
        if (chkbox4_Melody.getState())
        {
          jmelodygen = new JMelodyGen( jrhythm );
          if (chkbox4_Bass.getState()) { jmelodygen.start_time += 2; }
          if (chkbox4_Drum.getState()) { jmelodygen.start_time += 2; }
          jmelodygen.bar_num = Integer.valueOf( txt2_BarNum.getText() ).intValue();
          jmelodygen.generate();
          jmelodygen.transform();
          score.addPart( jmelodygen.melody_part );
        }

        // Bass
        if (chkbox4_Bass.getState())
        {
          if (chkbox4_Melody.getState())
          {
            jbassgen = new JBassGen( jmelodygen.melody_jnote, jrhythm.beat_num );
          }
          else
          {
            jbassgen = new JBassGen();
          }
          if (chkbox4_Melody.getState() || chkbox4_Bird.getState()) { jbassgen.start_bar += 2; }
          if (chkbox4_Drum.getState()) { jbassgen.start_time += 2; }
          jbassgen.bar_num = Integer.valueOf( txt2_BarNum.getText() ).intValue();
          jbassgen.generate();
          jbassgen.transform();
          score.addPart( jbassgen.bass_part );
        }

        // Bird
        if (chkbox4_Bird.getState())
        {
          jbirdgen = new JBirdGen();
          if (chkbox4_Bass.getState()) { jbirdgen.start_time += 2; }
          if (chkbox4_Drum.getState()) { jbirdgen.start_time += 2; }
          jbirdgen.bar_num = Integer.valueOf( txt2_BarNum.getText() ).intValue();
          jbirdgen.generate();
          jbirdgen.transform();
          score.addPart( jbirdgen.bird_part ); 
        }

        // Drum
        if (chkbox4_Drum.getState())
        {
          jdrumgen = new JDrumGen();
          if (chkbox4_Melody.getState() || chkbox4_Bird.getState()) { jdrumgen.start_bar += 2; }
          if (chkbox4_Bass.getState()) { jdrumgen.start_bar += 2; }
          jdrumgen.bar_num = Integer.valueOf( txt2_BarNum.getText() ).intValue();
          jdrumgen.generate();
          jdrumgen.transform();
          score.addPart( jdrumgen.drum_part );
        }

        btn5_View.setEnabled( true );
        btn5_Play.setEnabled( true );
        btn5_Save.setEnabled( true );
      }
      else if (evt.getSource() == btn5_View)
      {
        View.notate( score );
      }
      else if (evt.getSource() == btn5_Play)
      {
        if (btn5_Play.getLabel().equals( "Play" ))
        {
          btn1_RhyGen.setEnabled( false );
          btn1_RhyApply.setEnabled( false );
          btn5_Gen.setEnabled( false );
          btn5_View.setEnabled( false );
          btn5_Play.setLabel( "Stop" );
          btn5_Save.setEnabled( false );
          
          score.setTempo( Integer.valueOf( txt3_Tempo.getText() ).intValue() );
          Play.midiCycle( score );
        }
        else
        {
          //Play.midiCycle( new Score("Empty Score") );
          Play.stopCycle();
          Play.stopMidi();
          while (Play.cycleIsPlaying()) ;

          btn1_RhyGen.setEnabled( true );
          btn1_RhyApply.setEnabled( true );
          btn5_Gen.setEnabled( true );
          btn5_View.setEnabled( true );
          btn5_Play.setLabel( "Play" );
          btn5_Save.setEnabled( true );
        }
      }
      else if (evt.getSource() == btn5_Save)
      {
        Write.midi(score, "jasonmel.mid");
      }
    }
  }

  /**
   * Main Function
   * @param args arguments...
   */
  public static void main(String[] args)
  {
    JMIDIGen jmidigen = new JMIDIGen();

    jmidigen.addWindowListener (
      new WindowAdapter() {
        public void windowClosing( WindowEvent e )
        {
          System.exit(0);
        }
      }
    );
  }
}

