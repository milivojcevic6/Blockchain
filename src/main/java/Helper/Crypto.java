package Helper;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Crypto {
    private KeyPair keyPair;

    public Crypto(){
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048,new SecureRandom());
            this.keyPair = generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String sign(String plainText){
        try {
            Signature privateSignature= Signature.getInstance("SHA256withRSA");
            privateSignature.initSign(this.keyPair.getPrivate());
            privateSignature.update(plainText.getBytes());

            byte[] signature = privateSignature.sign();//256
            return Base64.getEncoder().encodeToString(signature);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean verify(String plainText, String signature, String publicKey){
        byte[] byteKey = Base64.getMimeDecoder().decode(publicKey);
        try {
            Signature publicSignature = Signature.getInstance("SHA256withRSA");
            X509EncodedKeySpec X509pKeySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicSignature.initVerify(keyFactory.generatePublic(X509pKeySpec));
            publicSignature.update(plainText.getBytes());
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return publicSignature.verify(signatureBytes);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }


    public String getPublicKey(){
        return new String(Base64.getMimeEncoder().encode(keyPair.getPublic().getEncoded()));
    }
}
