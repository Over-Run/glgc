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
 * The OpenGL shader.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class Shader implements GLObject {
    private static final Cleaner CLEANER = Cleaner.create();
    private final State state;
    private final Cleaner.Cleanable cleanable;

    private Shader(GL20C gl, int id) {
        this.state = new State(gl, id);
        this.cleanable = CLEANER.register(this, state);
    }

    private record State(GL20C gl, int id) implements Runnable {
        @Override
        public void run() {
            gl.deleteShader(id);
        }
    }

    /**
     * Creates an OpenGL shader with {@link GL20C#createShader(int)}.
     *
     * @param gl   the OpenGL context
     * @param type the shader type
     * @return the OpenGL shader
     */
    public static Shader create(GL20C gl, int type) {
        return new Shader(gl, gl.createShader(type));
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
