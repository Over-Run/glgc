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

import overrungl.opengl.GL30C;
import overrungl.opengl.GL45C;

import java.lang.ref.Cleaner;

/**
 * The OpenGL vertex array object.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class VAO implements GLObject {
    private static final Cleaner CLEANER = Cleaner.create();
    private final State state;
    private final Cleaner.Cleanable cleanable;

    private VAO(GL30C gl, int id) {
        this.state = new State(gl, id);
        this.cleanable = CLEANER.register(this, state);
    }

    private record State(GL30C gl, int id) implements Runnable {
        @Override
        public void run() {
            gl.deleteVertexArrays(id);
        }
    }

    /**
     * Generates an OpenGL vertex array object with {@link GL30C#genVertexArrays()}.
     *
     * @param gl the OpenGL context
     * @return the OpenGL vertex array object
     */
    public static VAO gen(GL30C gl) {
        return new VAO(gl, gl.genVertexArrays());
    }

    /**
     * Creates an OpenGL vertex array object with {@link GL45C#createVertexArrays()}.
     *
     * @param gl  the OpenGL context
     * @param <T> the OpenGL context type
     * @return the OpenGL vertex array object
     */
    public static <T extends GL30C & GL45C> VAO create(T gl) {
        return new VAO(gl, gl.createVertexArrays());
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
