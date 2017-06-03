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

import java.io.IOException;
import java.net.InetAddress;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fred
 */
class AccessCheckerTask extends TimerTask
{

    private int delay = 60_000;

    AccessCheckerTask()
    {
    }

    /**
     * Get the value of delay
     *
     * @return the value of delay
     */
    public int getDelay()
    {
        return delay;
    }

    /**
     * Set the value of delay
     *
     * @param delay new value of delay
     */
    public void setDelay( int delay )
    {
        this.delay = delay;
    }

    // run is a abstract method that defines task performed at scheduled time.
    public void run()
    {
        byte address[] =
        {
            8, 8, 8, 8
        };
        Logger.getLogger( AccessCheckerTask.class.getName() ).log( Level.INFO, "checking connection.." );
        try
        {
            HalBuddy.setConnected( InetAddress.getByAddress( address ).isReachable( 1000 ) );
        }
        catch ( IOException ex )
        {
            Logger.getLogger( AccessCheckerTask.class.getName() ).log( Level.SEVERE, null, ex );
            HalBuddy.setConnected( false );
        }
    }

}
