package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;

import org.telegram.bautrukevich.AndroidUtilities;
import org.telegram.bautrukevich.Utilities;

import java.util.ArrayList;

public class TimerParticles {

    private long lastAnimationTime;

    private static class Particle {
        float x;
        float y;
        float vx;
        float vy;
        float velocity;
        float alpha;
        float lifeTime;
        float currentTime;
    }

    private ArrayList<Particle> particles = new ArrayList<>();
    private ArrayList<Particle> freeParticles = new ArrayList<>();

    public TimerParticles() {
        for (int a = 0; a < 40; a++) {
            freeParticles.add(new Particle());
        }
    }

    private void updateParticles(long dt) {
        int count = particles.size();
        for (int a = 0; a < count; a++) {
            Particle particle = particles.get(a);
            if (particle.currentTime >= particle.lifeTime) {
                if (freeParticles.size() < 40) {
                    freeParticles.add(particle);
                }
                particles.remove(a);
                a--;
                count--;
                continue;
            }
            particle.alpha = 1.0f - AndroidUtilities.decelerateInterpolator.getInterpolation(particle.currentTime / particle.lifeTime);
            particle.x += particle.vx * particle.velocity * dt / 500.0f;
            particle.y += particle.vy * particle.velocity * dt / 500.0f;
            particle.currentTime += dt;
        }
    }

    public void draw(Canvas canvas, Paint particlePaint, RectF rect, float radProgress, float alpha) {
        int count = particles.size();
        for (int a = 0; a < count; a++) {
            Particle particle = particles.get(a);
            particlePaint.setAlpha((int) (255 * particle.alpha * alpha));
            canvas.drawPoint(particle.x, particle.y, particlePaint);
        }

        double vx = Math.sin(Math.PI / 180.0 * (radProgress - 90));
        double vy = -Math.cos(Math.PI / 180.0 * (radProgress - 90));
        float rad = rect.width() / 2;
        float cx = (float) (-vy * rad + rect.centerX());
        float cy = (float) (vx * rad + rect.centerY());
        for (int a = 0; a < 1; a++) {
            Particle newParticle;
            if (!freeParticles.isEmpty()) {
                newParticle = freeParticles.get(0);
                freeParticles.remove(0);
            } else {
                newParticle = new Particle();
            }
            newParticle.x = cx;
            newParticle.y = cy;

            double angle = (Math.PI / 180.0) * (Utilities.random.nextInt(140) - 70);
            if (angle < 0) {
                angle = Math.PI * 2 + angle;
            }
            newParticle.vx = (float) (vx * Math.cos(angle) - vy * Math.sin(angle));
            newParticle.vy = (float) (vx * Math.sin(angle) + vy * Math.cos(angle));

            newParticle.alpha = 1.0f;
            newParticle.currentTime = 0;

            newParticle.lifeTime = 400 + Utilities.random.nextInt(100);
            newParticle.velocity = 20.0f + Utilities.random.nextFloat() * 4.0f;
            particles.add(newParticle);
        }

        long newTime = SystemClock.elapsedRealtime();
        long dt = Math.min(20, (newTime - lastAnimationTime));
        updateParticles(dt);
        lastAnimationTime = newTime;
    }
}
