package card;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class ReadCard {

    public static byte[] SelectAPDU = new byte[] { (byte) 0x00, (byte) 0xA4,
            (byte) 0x04, (byte) 0x00, (byte) 0x10, (byte) 0xD1, (byte) 0x58,
            (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x11, (byte) 0x00 };
    public static byte[] ReadProfileAPDU = new byte[] { (byte) 0x00,
            (byte) 0xca, (byte) 0x11, (byte) 0x00, (byte) 0x02, (byte) 0x00,
            (byte) 0x00 };

    public static void main(String[] args) {

        TerminalFactory terminalFactory = TerminalFactory.getDefault();

        try {
            for (CardTerminal terminal : terminalFactory.terminals().list()) {
                try {
                    Card card = terminal.connect("*");
                    CardChannel channel = card.getBasicChannel();

                    CommandAPDU command = new CommandAPDU(SelectAPDU);
                    ResponseAPDU response = channel.transmit(command);

                    command = new CommandAPDU(ReadProfileAPDU);
                    response = channel.transmit(command);

                    System.out.println(new String(Arrays.copyOfRange(
                            response.getData(), 0, 12))); // 卡號
                    System.out.println(new String(Arrays.copyOfRange(
                            response.getData(), 12, 32), "Big5").trim()); // 姓名
                    System.out.println(new String(Arrays.copyOfRange(
                            response.getData(), 32, 42))); // 身分證號
                    System.out.println(new String(Arrays.copyOfRange(
                            response.getData(), 42, 49))); // 出生年月日
                    System.out.println(new String(Arrays.copyOfRange(
                            response.getData(), 49, 50))); // 性別

                    System.out.println(new String(Arrays.copyOfRange(
                            response.getData(), 50, 57))); // 發卡年月日

                } catch (javax.smartcardio.CardNotPresentException e) {
                    // e.printStackTrace();
                    continue;
                } catch (CardException e) {
                    // e.printStackTrace();
                    continue;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
        }

    }
}
