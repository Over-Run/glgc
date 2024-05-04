/*
 * MIT License
 *
 * Copyright (c) 2024 Overrun Organization
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 */

package overrun.glgc;

import overrungl.opengl.GL20C;

import java.lang.ref.Cleaner;

/**
 * The OpenGL program.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class Program implements GLObject {
    private static final Cleaner CLEANER = Cleaner.create();
    private final State state;
    private final Cleaner.Cleanable cleanable;

    private Program(GL20C gl, int id) {
        this.state = new State(gl, id);
        this.cleanable = CLEANER.register(this, state);
    }

    private record State(GL20C gl, int id) implements Runnable {
        @Override
        public void run() {
            gl.deleteProgram(id);
        }
    }

    /**
     * Creates an OpenGL program with {@link GL20C#createProgram()}.
     *
     * @param gl the OpenGL context
     * @return the OpenGL program
     */
    public static Program create(GL20C gl) {
        return new Program(gl, gl.createProgram());
    }

    @Override
    public int id() {
        return state.id();
    }

    @Override
    public void close() {
        cleanable.clean();
    }
}
