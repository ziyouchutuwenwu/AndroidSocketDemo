package ClientSocket.Foundation.header;

public class PkgHeaderOption {
    public int HeaderSize;
    public int MaxDataSize;
    public int HeaderFrameLenth;

    public static PkgHeaderOption getPkgOptionWithHeaderSize(int headerSize){
        PkgHeaderOption pkgHeaderOption = new PkgHeaderOption();

        if (headerSize == 0) pkgHeaderOption.HeaderSize = 2;
        if (headerSize !=2 && headerSize !=4) return null;
        pkgHeaderOption.HeaderSize = headerSize;

        switch (pkgHeaderOption.HeaderSize) {
            case 2:
                pkgHeaderOption.MaxDataSize = 0xFFFF;
                pkgHeaderOption.HeaderFrameLenth = 2;
            case 4:
                pkgHeaderOption.MaxDataSize = 0x7FFFFFFF;
                pkgHeaderOption.HeaderFrameLenth = 4;
        }

        return pkgHeaderOption;
    }
}