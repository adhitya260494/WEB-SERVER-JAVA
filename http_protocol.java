
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;


public class http_protocol {
    private Socket client;

    public http_protocol(Socket s) {
        this.client = s;
    }

    public void handleRequest() throws IOException {
        try {
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        if(br == null){
            throw new IOException("ada Request dari browser tapi data requestnya kosong");
        }
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        int contentLength = 0;
        /*if ((line = br.readLine()).isBlank()){
            System.exit(0);
        }*/
        while (!(line = br.readLine()).isBlank()) {
            requestBuilder.append(line + "\r\n");
            if(line.toLowerCase().startsWith("content-length")){
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }
        StringBuilder requestBodyBuilder = new StringBuilder();
        if (contentLength > 0){
            int read;
            while ((read = br.read()) != -1){
                requestBodyBuilder.append((char) read);
                if (requestBodyBuilder.length() == contentLength)
                break;
            }
            requestBuilder.append("\r\n" + requestBodyBuilder);
        }
        String request =  requestBuilder.toString();
        if (request.isEmpty()){
            throw new IOException("KOSONG");
        }
        System.out.println(request + "dari client");
        System.out.println(request);

        String[] requestsLines = request.split("\r\n");
        String[] requestLine = requestsLines[0].split(" ");
        String method = requestLine[0];
        String path = requestLine[1];
        String version = requestLine[2];
        String host = requestsLines[1].split(" ")[1];

       /*  List<String> headers = new ArrayList<>();
        for (int h = 2; h < requestsLines.length; h++) {
            String header = requestsLines[h];
            headers.add(header);
        }
        String accessLog = String.format("client %s, method %s, path %s, version %s, host %s, headers %s",
                this.client.toString(), method, path, version, host, headers.toString());
        System.out.println(accessLog);
*/
        if (method.equals("GET")) {
            String parameter_query_string = "";
            CharSequence qry_string = "?";
            Boolean ketemu = path.contains(qry_string);
            if(ketemu == true){
                String[] parse_path;
                parse_path = path.split("\\?");
                path = parse_path[0].toString();
                if(parse_path.length > 1)
                    parameter_query_string =" --"+parse_path[1].toString().replace("&"," --");
            }
            //system.out.println(parameter_query_string);
            Path filePath = getFilePath(path);
            if (Files.exists(filePath)) {
                // file ditemukan
                String contentType = guessContentType(filePath);
                //tambahan untuk ngecek ekstensi file jenis html atau php
                String extension = filePath.toString().toLowerCase();
                if(extension.endsWith(".php")){     
                byte[] script_php = callPHP(filePath.toString().toLowerCase(),"GET",parameter_query_string);
                    if(script_php.length > 0)
                        handleResponse(version, "200 OK", "text/html", script_php);
                        else{
                            byte[] error_php = "<h1> Internal Server Erro</h1>".getBytes();
                            handleResponse(version, "500 Internal Server Error", "text/html",error_php);
                        }                  
               } else {
               handleResponse(version,"200 OK", contentType, Files.readAllBytes(filePath));

               }
             } else {
                // file tidak ada : status error 404
                byte[] notFoundContent = "<h1> Not Found:(</h1>".getBytes();
                handleResponse(version,"404 Not FOund", "text/html", notFoundContent);
            }
        } else 
        {
            if(method.equals("POST")){
            Path filePath = getFilePath(path);
            System.out.println(filePath);
            if (Files.exists(filePath)){
                //file ditemukan
                String contentType = guessContentType(filePath);
                // tambahan untuk ngecek ekstensi file jenis html atau php
                String extension = filePath.toString().toLowerCase();

                String query_string =" --"+requestsLines[requestsLines.length-1].toString().replace("&", " --");

                    if(extension.endsWith(".php")){
                        byte[] script_php = callPHP(filePath.toString().toLowerCase(),"POST",query_string);
                        if(script_php.length > 0){
                            handleResponse(version, "200 OK","text/html", script_php);

                        }else {
                                byte[] error_php = "<h1>Internal Server Error </h1>".getBytes();
                                handleResponse(version,"500 Internal Server Error","text/html", error_php);
                            }
                    } else {
                        handleResponse(version,"200 OK",contentType,Files.readAllBytes(filePath));
                    }
            } else {
                //file tidak ada : status error 404
                byte[] notFoundContent = "<h1> FIle Not Found </h1>".getBytes();
                handleResponse(version, "404 Not FOund", "text/html", notFoundContent);
            }
        }
    } 
    }   catch(IOException e){
        System.out.println("error");
        e.printStackTrace();
    }

}

    private void handleResponse(String version, String status, String contentType, byte[] content) throws IOException {
        OutputStream clientOutput = this.client.getOutputStream();
        clientOutput.write((version+" "+ status + "\r\n").getBytes());
        clientOutput.write(("ContentType: " + contentType + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write(content);
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.flush();
        this.client.close();
    }

    private static Path getFilePath(String path) {
        if ("/".equals(path)) {
            path = "/index.html";
        }
        // silahkan ganti folder anda sesuai dengan konfigurasi laptop
        String folder = "C:\\xampp\\htdocs\\java";
        return Paths.get(folder, path);
    }

    private static String guessContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }
    //tambahan untuk menjalankan script php
    private byte[] callPHP(String filePath, String method, String params) throws IOException{
        String result = "";
        try {
            //execute command
            String command = "php bridge.php"+" --target="+filePath+" --method="+method+params;
            System.out.println(command);
            Process child = Runtime.getRuntime().exec(command);
            int exitCode = child.waitFor();
            if (exitCode != 0){
                //throw new ioexception ("command exited with " + exitcode);
                System.out.println("php error dengan kode : "+ exitCode);
            } else {
            DataInputStream in = new DataInputStream(child.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null){
                result +=line;
            }
            in.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.getBytes();
    }
}
