package top.infra.filesafe;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import top.infra.logging.Logger;

public class ClearFileGpgNative extends AbstractResource implements ClearFile {

    private final String gpgExecutable;
    private final Map<String, String> environment;

    public ClearFileGpgNative(final Logger logger, final Path path, final String gpgExecutable) {
        super(logger, path);
        this.gpgExecutable = gpgExecutable;

        final Map<String, String> env = new LinkedHashMap<>();
        env.put("LC_CTYPE", "UTF-8");
        this.environment = Collections.unmodifiableMap(env);
    }

    @Override
    public void encrypt(final String passphrase, final Path targetPath) {
        logger.info(String.format("    Encrypting [%s] by native gpg", this.getPath()));
        // echo ${CI_OPT_GPG_PASSPHRASE} |
        // gpg --yes --passphrase-fd 0 --cipher-algo AES256 --symmetric --no-symkey-cache --output src/test/resources/testfile.txt.enc
        // src/test/resources/testfile.txt
        final List<String> gpgDecrypt = GpgUtils.cmdGpgBatchYes(
            this.gpgExecutable,
            "--passphrase-fd", "0",
            "--cipher-algo", "AES256", "--symmetric", "--no-symkey-cache",
            "--output", targetPath.toString(),
            this.getPath().toString()
        );
        final Entry<Integer, String> resultGpgDecrypt = this.exec(passphrase, gpgDecrypt);
        logger.info(resultGpgDecrypt.getValue());
    }

    @Override
    protected Map<String, String> getEnvironment() {
        return this.environment;
    }
}