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

/**
 *
 * @author Fred
 */
public class BlinkPattern extends LedPattern
{

    private int period = 500;

    public int getPeriod()
    {
        return period;
    }

    public void setPeriod( int period )
    {
        this.period = period;
    }

    @Override
    int compute()
    {
        Long time = System.currentTimeMillis();
        return time % ( period ) < ( period / 2 ) ? 0 : 1024;
    }

}
