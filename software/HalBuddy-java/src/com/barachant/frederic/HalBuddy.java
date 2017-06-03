/*
 * <HalBuddy, a small thingy to help you for small things.>
 * Copyright (C) 2017  Frederic Barachant
 *
 * This file is part of HalBuddy.
 *
 * HalBuddy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * HalBuddy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HalBuddy.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.barachant.frederic;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import static spark.Spark.get;

/**
 *
 * @author Fred
 */
public class HalBuddy
{

    private static AccessCheckerTask accessChecker;
    private static ButtonBehavior behavior;
    private static GpioPinDigitalInput button;
    private static boolean connected = false;
    private static LedManagerTask ledManagerTask;
    private static GpioPinPwmOutput pwm;
    private static Timer timer;
    static final GpioController gpio = GpioFactory.getInstance();

    private static void setupHardware()
    {
        pwm = gpio.provisionPwmOutputPin( RaspiPin.GPIO_23 );

        button = gpio.provisionDigitalInputPin( RaspiPin.GPIO_12 );
        button.setPullResistance( PinPullResistance.PULL_UP );//so that it is only LOW when button is pressed.
        button.setDebounce( 100, PinState.LOW );//so that nothing occurs if press lasts less than that.
        // this is related to the PWM frequencies and modes to get a nice result.
        Gpio.pwmSetMode( com.pi4j.wiringpi.Gpio.PWM_MODE_MS );
        Gpio.pwmSetRange( 1024 );
        Gpio.pwmSetClock( 10 );// high frequency pwm so that we see no flicker. mosfet can accept the frequency, whichever it is.
    }

    private static void setupParallelTasks()
    {
        timer = new Timer();
        ledManagerTask = new LedManagerTask( pwm );
        accessChecker = new AccessCheckerTask();

        // this will manage the LED behavior.
        timer.scheduleAtFixedRate( ledManagerTask, 0, 20 );
        // we enable a constant monitoring function so that we can report if we are still connected.
        timer.scheduleAtFixedRate( accessChecker, 0, 60000 );
    }

    public static ButtonBehavior getBehavior()
    {
        return behavior;
    }

    public static void setBehavior( ButtonBehavior behavior )
    {
        HalBuddy.behavior = behavior;
    }

    public static LedManagerTask getLedManagerTask()
    {
        return ledManagerTask;
    }

    /**
     * @return the connected
     */
    public static boolean isConnected()
    {
        return connected;
    }

    /**
     * @param aConnected the connected to set
     */
    public static void setConnected( boolean aConnected )
    {
        if ( null != behavior )
        {
            behavior.ConnectionStatusChange( aConnected );
        }
    }

    public static void main( String[] args )
    {
        try
        {
            Thread.setDefaultUncaughtExceptionHandler( new Thread.UncaughtExceptionHandler()
            {

                @Override
                public void uncaughtException( Thread t, Throwable e )
                {
                    Logger.getGlobal().log( Level.SEVERE, "uncaught exception", e );
                }
            } );
            FileHandler fh = new FileHandler( "log/app.log" );
            Logger.getGlobal().addHandler( fh );
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter( formatter );
            behavior = new DefaultButtonBehavior();
        }
        catch ( IOException ex )
        {
            Logger.getGlobal().log( Level.SEVERE, null, ex );
        }
        catch ( SecurityException ex )
        {
            Logger.getGlobal().log( Level.SEVERE, null, ex );
        }

        // prepare the hardware for the task, that is GPIO for button and PWM for the LED.
        setupHardware();

        // we setup the basic tasks we need now.
        setupParallelTasks();

        button.addListener( new GpioPinListenerDigital()
        {

            @Override
            public void handleGpioPinDigitalStateChangeEvent( GpioPinDigitalStateChangeEvent event )
            {
                //TODO: cancel if we see a falling for more than a certain period. (then flash to indicate cancellation and not take care of following rising edge)
                if ( null != HalBuddy.behavior )
                {
                    if ( event.getEdge() == PinEdge.RISING )
                    {
                        HalBuddy.behavior.pressed();
                    }
                    else
                    {
                        HalBuddy.behavior.released();
                    }
                }
            }
        } );

        get( "/version", (req, res) -> "1.0" );
        //TODO: should be posts...
        get( "/shutdown", (req, res) -> new ProcessBuilder( "shutdown now&" ).start() );
        get( "/quit", (req, res) ->
        {
            new Thread( () ->
            {
                try
                {
                    Thread.sleep( 1000 );
                }
                catch ( InterruptedException ex )
                {
                    Logger.getLogger( HalBuddy.class.getName() ).log( Level.SEVERE, null, ex );
                }
                System.exit( 0 );
            }
            ).start();
            return "exiting in a second";
        }
        );
        get( "/button", (req, res) -> button.getState() );
    }

}
