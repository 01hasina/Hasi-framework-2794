package mg.itu.prom16.util;


import java.io.IOException;
import java.io.InputStream;

public class PartFile {
    private String fileName;
    private String contentType;
    private long size;
    private byte[] content; // Contenu du fichier sous forme de tableau d'octets

    // Constructeur
    public PartFile(String fileName, String contentType, long size, byte[] content) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.content = content;
    }

    // Méthode pour obtenir le nom du fichier
    public String getFileName() {
        return fileName;
    }

    // Méthode pour obtenir le type MIME
    public String getContentType() {
        return contentType;
    }

    // Méthode pour obtenir la taille du fichier
    public long getSize() {
        return size;
    }

    // Méthode pour obtenir un InputStream des données du fichier
    public InputStream getInputStream() throws IOException {
        return new java.io.ByteArrayInputStream(content);
    }

    // Méthode pour obtenir le contenu sous forme de tableau d'octets
    public byte[] getContent() {
        return content;
    }

    // Méthode pour sauvegarder le fichier sur le disque
    public void write(String path) throws IOException {
        java.nio.file.Files.write(java.nio.file.Paths.get(path), content);
    }

    // Méthode pour libérer les ressources (similaire à delete() dans Part)
    public void delete() {
        content = null;  // Supprime le contenu du fichier de la mémoire
    }
    public void transferer(String pathDirectory) throws IOException {
        if (pathDirectory == null || pathDirectory.isEmpty()) {
            throw new IllegalArgumentException("Le répertoire de destination ne peut pas être vide ou null.");
        }
    
        // Créer le chemin complet vers le fichier dans le répertoire de destination
        java.nio.file.Path directoryPath = java.nio.file.Paths.get(pathDirectory);
        
        // Vérifier si le répertoire existe, sinon le créer
        if (!java.nio.file.Files.exists(directoryPath)) {
            java.nio.file.Files.createDirectories(directoryPath); // Crée tous les répertoires nécessaires
        }
    
        // Créer le chemin du fichier complet en combinant le répertoire et le nom de fichier
        java.nio.file.Path filePath = directoryPath.resolve(this.fileName);
    
        // Écrire le fichier sur le disque
        java.nio.file.Files.write(filePath, this.content);
        
        System.out.println("Fichier transféré avec succès à : " + filePath.toString());
    }
    
}
