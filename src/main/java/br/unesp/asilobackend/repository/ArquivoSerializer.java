package br.unesp.asilobackend.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class ArquivoSerializer {

    public <T> boolean escreverArquivo(List<T> objects, String caminho) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminho))) {
            oos.writeObject(objects);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object lerArquivo(String caminho) {
        File arquivo = new File(caminho);

        if (!arquivo.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(caminho))) {
            return ois.readObject();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            // Se o arquivo estiver corrompido, vazio, ou as classes mudaram (ex: SerialVersionUID)
            e.printStackTrace();
            return null;
        }
    }
}
