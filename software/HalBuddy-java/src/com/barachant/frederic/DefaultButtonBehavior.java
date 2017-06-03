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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fred
 */
public class DefaultButtonBehavior implements ButtonBehavior
{

    private static BlinkPattern blink = new BlinkPattern();

    private static BreathPattern breath = new BreathPattern();
    private static StaticPattern full = new StaticPattern();

    @Override
    public void ConnectionStatusChange( boolean isConnected )
    {

        if ( true == isConnected )
        {
            // Pi is connected to internet, we can now breath freely.
            HalBuddy.getLedManagerTask().setPattern( breath );
        }
        else
        {
            //panic.
            blink.setPeriod( 1000 );
            HalBuddy.getLedManagerTask().setPattern( blink );
        }
    }

    @Override
    public void pressed()
    {
        //set light to median blink mode to indicate that the order was taken in account.
        blink.setPeriod( 500 );
        HalBuddy.getLedManagerTask().setPattern( blink );
        // call URL in configuration
        try
        {
            Thread.sleep( 2000 );// just blink for two seconds. Implement your behavior here.
        }
        catch ( InterruptedException ex )
        {
            Logger.getGlobal().log( Level.SEVERE, null, ex );
        }
        HalBuddy.getLedManagerTask().setPattern( breath );
        blink.setPeriod( 500 );
    }

    @Override
    public void released()
    {
        HalBuddy.getLedManagerTask().setPattern( breath );
    }

}
