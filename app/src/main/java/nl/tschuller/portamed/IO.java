package nl.tschuller.portamed;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;

import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKeys;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class IO {

    Context context;

    public IO(Context context) {
        this.context = context;
    }

    public Serializable readEncryptedFile(String filename) throws GeneralSecurityException, IOException, ClassNotFoundException {


// Although you can define your own key generation parameter specification, it's
// recommended that you use the value specified here.
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

        File file = new File(context.getFilesDir(), filename);
        if(!file.exists()) return null;

        EncryptedFile encryptedFile = new EncryptedFile.Builder(
                file,
                context,
                mainKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build();

        InputStream inputStream = encryptedFile.openFileInput();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Serializable s = (Serializable) objectInputStream.readObject();
        objectInputStream.close();

        return s;
    }

    public void writeEncryptedFile(String filename, Serializable object) throws IOException, GeneralSecurityException {

// Although you can define your own key generation parameter specification, it's
// recommended that you use the value specified here.
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

// Create a file with this name or replace an entire existing file
// that has the same name. Note that you cannot append to an existing file,
// and the filename cannot contain path separators.

        File file = new File(context.getFilesDir(), filename);
        if(!file.exists() || file.delete()) {

            EncryptedFile encryptedFile = new EncryptedFile.Builder(
                    file,
                    context,
                    mainKeyAlias,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build();

            OutputStream outputStream = encryptedFile.openFileOutput();

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();
        }else{
            System.err.println("couldn't delete file");
        }
    }


}
