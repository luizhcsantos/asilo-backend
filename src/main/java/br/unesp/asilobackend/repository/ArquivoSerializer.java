package br.unesp.asilobackend.repository;

import br.unesp.asilobackend.domain.Administrador;
import br.unesp.asilobackend.domain.Doador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArquivoSerializer {

	public boolean escreverArquivo(List<Doador> doadores, String caminho) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminho))){
            oos.writeObject(doadores);
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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

    public boolean escreverArquivoAdmin(List<Administrador> admins, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(admins); // Serializa e escreve a lista de admins
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
