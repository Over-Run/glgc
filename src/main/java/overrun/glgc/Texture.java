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

import overrungl.opengl.GL11C;
import overrungl.opengl.GL45C;

import java.lang.ref.Cleaner;

/**
 * The OpenGL texture.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class Texture implements GLObject {
    private static final Cleaner CLEANER = Cleaner.create();
    private final State state;
    private final Cleaner.Cleanable cleanable;

    private Texture(GL11C gl, int id) {
        this.state = new State(gl, id);
        this.cleanable = CLEANER.register(this, state);
    }

    private record State(GL11C gl, int id) implements Runnable {
        @Override
        public void run() {
            gl.deleteTextures(id);
        }
    }

    /**
     * Generates an OpenGL texture with {@link GL11C#genTextures()}.
     *
     * @param gl the OpenGL context
     * @return the OpenGL texture
     */
    public static Texture gen(GL11C gl) {
        return new Texture(gl, gl.genTextures());
    }

    /**
     * Creates an OpenGL texture with {@link GL45C#createTextures(int)}.
     *
     * @param gl     the OpenGL context
     * @param target the texture target
     * @param <T>    the OpenGL context type
     * @return the OpenGL texture
     */
    public static <T extends GL11C & GL45C> Texture create(T gl, int target) {
        return new Texture(gl, gl.createTextures(target));
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
