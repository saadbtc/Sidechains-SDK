package com.horizen.fixtures;

import com.horizen.secret.VrfKeyGenerator;
import com.horizen.proof.VrfProof;
import com.horizen.secret.VrfSecretKey;
import com.horizen.vrf.VrfOutput;

import java.util.Random;

public class VrfGenerator {

    private static byte[] generateRandomMessage(long seed) {
        Random rnd = new Random(seed);
        byte[] randomBytes = new byte[32];
        rnd.nextBytes(randomBytes);
        randomBytes[31] = 0; // nullify last byte to fit Tweedle 254bit FE.
        return randomBytes;
    }

    public static VrfProof generateProof(long seed) {
        Random rnd = new Random(seed);
        byte[] vrfSeed = new byte[32];
        rnd.nextBytes(vrfSeed);
        VrfSecretKey vrfSecretKey = VrfKeyGenerator.getInstance().generateSecret(vrfSeed);
        byte[] randomBytes = generateRandomMessage(seed);
        return vrfSecretKey.prove(randomBytes).getKey();
    }

    public static VrfOutput generateVrfOutput(long seed) {
        Random rnd = new Random(seed);
        byte[] vrfSeed = new byte[32];
        rnd.nextBytes(vrfSeed);
        VrfSecretKey vrfSecretKey = VrfKeyGenerator.getInstance().generateSecret(vrfSeed);
        byte[] randomBytes = generateRandomMessage(seed);
        VrfProof proof = vrfSecretKey.prove(randomBytes).getKey();
        return proof.proofToVrfOutput(vrfSecretKey.publicImage(), randomBytes).get();
    }
}
