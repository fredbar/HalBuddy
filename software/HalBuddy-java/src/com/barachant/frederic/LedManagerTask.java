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

import com.pi4j.io.gpio.GpioPinPwmOutput;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fred
 */
class LedManagerTask extends TimerTask
{

    private LedPattern pattern;

    private final GpioPinPwmOutput pwm;

    LedManagerTask( GpioPinPwmOutput pwm )
    {
        this.pwm = pwm;
    }

    public LedPattern getPattern()
    {
        return pattern;
    }

    public void setPattern( LedPattern pattern )
    {
        this.pattern = pattern;
    }

    // run is a abstract method that defines task performed at scheduled time.
    public void run()
    {
        if ( null != pattern )
        {
            try
            {
                pwm.setPwm( pattern.compute() );
            }
            catch ( Exception e )
            {
                Logger.getGlobal().log( Level.SEVERE, "error in led management.", e );
            }
        }
    }

}
