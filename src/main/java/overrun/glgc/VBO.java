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

import overrungl.opengl.GL15C;
import overrungl.opengl.GL45C;

import java.lang.ref.Cleaner;

/**
 * The OpenGL vertex buffer object.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class VBO implements GLObject {
    private static final Cleaner CLEANER = Cleaner.create();
    private final State state;
    private final Cleaner.Cleanable cleanable;

    private VBO(GL15C gl, int id) {
        this.state = new State(gl, id);
        this.cleanable = CLEANER.register(this, state);
    }

    private record State(GL15C gl, int id) implements Runnable {
        @Override
        public void run() {
            gl.deleteBuffers(id);
        }
    }

    /**
     * Generates an OpenGL vertex buffer object with {@link GL15C#genBuffers()}.
     *
     * @param gl the OpenGL context
     * @return the OpenGL vertex buffer object
     */
    public static VBO gen(GL15C gl) {
        return new VBO(gl, gl.genBuffers());
    }

    /**
     * Creates an OpenGL vertex buffer object with {@link GL45C#createBuffers()}.
     *
     * @param gl  the OpenGL context
     * @param <T> the OpenGL context type
     * @return the OpenGL vertex buffer object
     */
    public static <T extends GL15C & GL45C> VBO create(T gl) {
        return new VBO(gl, gl.createBuffers());
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
