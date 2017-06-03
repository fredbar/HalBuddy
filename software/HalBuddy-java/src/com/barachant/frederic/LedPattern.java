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
 * Behavior of lighting. Implementors can setup a certain type of lighting, to
 * be used by the led manager. Led manager will call the compute() method in a
 * timely manner.
 *
 * @author Fred
 */
abstract public class LedPattern
{

    /**
     * computes the actual pattern value, based on time or whatever this pattern
     * is based on.
     *
     * @return an integer between 0 and 1024, representing the intensity of the
     * LED.
     */
    abstract int compute();

}
