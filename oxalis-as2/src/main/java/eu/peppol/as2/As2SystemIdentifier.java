package eu.peppol.as2;

import javax.security.auth.x500.X500Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author steinar
 *         Date: 09.10.13
 *         Time: 10:22
 */
public class As2SystemIdentifier {

    public static final String CN_PATTERN = "CN=([^,]*),";
    public static final Pattern commonNamePattern = Pattern.compile(CN_PATTERN);

    public static final String AS2_NAME = "\\A[^\"\\\\]{1,128}\\Z";
    public static Pattern as2NamePattern = Pattern.compile(AS2_NAME);

    private final String as2Name;

    public As2SystemIdentifier(String as2Name) throws InvalidAs2SystemIdentifierException {
        this.as2Name = as2Name;
        Matcher matcher = as2NamePattern.matcher(as2Name);
        if (!matcher.matches()) {
            throw new InvalidAs2SystemIdentifierException(as2Name );
        }
    }

    /**
     * Constructs an As2SystemIdentifier using the value of the CN attribute of the X500Principal object.
     * @param x500Principal instance from which the CN attribute should be parsed.
     * @throws eu.peppol.as2.As2SystemIdentifier.InvalidAs2SystemIdentifierException
     */
    public As2SystemIdentifier(X500Principal x500Principal) throws InvalidAs2SystemIdentifierException {
        String distinguishedName = x500Principal.getName(X500Principal.RFC1779);
        Matcher m = commonNamePattern.matcher(distinguishedName);
        if (m.find()) {
            String commonName = m.group(1);
            as2Name = commonName;
        } else {
            throw new InvalidAs2SystemIdentifierException(distinguishedName);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        As2SystemIdentifier that = (As2SystemIdentifier) o;

        if (!as2Name.equals(that.as2Name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return as2Name.hashCode();
    }


    @Override
    public String toString() {
        return as2Name;
    }

    class InvalidAs2SystemIdentifierException extends InvalidAs2MessageException {
        private final String as2Name;

        public InvalidAs2SystemIdentifierException(String as2Name) {
            super("Invalid AS2 System Identifier: " + as2Name);
            this.as2Name = as2Name;
        }

        private String getAs2Name() {
            return as2Name;
        }
    }
}
