///*
// * Part of Simbrain--a java-based neural network kit
// * Copyright (C) 2005,2007 The Authors.  See http://www.simbrain.net/credits
// *
// * This program is free software; you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation; either version 2 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
// */
//package org.simbrain.workspace;
//
///**
// * A wrapper for a public getter in a Simbrain object. The first component of a
// * coupling, which "produces" values of type E for a corresponding consumer
// * object which wraps a setter method on another object. Producers are created
// * in the <code>AttributeManager</code> class, by way of a
// * <code>PotentialConsumer</code>.
// *
// * @author Jeff Yoshimi
// *
// * @param <E> the type of data returned by the getter.
// *
// * @see Attribute
// * @see AttributeManager
// */
//public interface Producer<E> extends Attribute {
//
//    /**
//     * Return the value for this producer.
//     *
//     * @return the value for this producer
//     */
//    E getValue();
//
//}
