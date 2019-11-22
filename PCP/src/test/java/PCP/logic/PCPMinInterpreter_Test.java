/*
 * this is a school project under "The Unlicence".
 */
package PCP.logic;

import PCP.Min.data.*;
import PCP.Min.logic.PCPMinInterpreter;
import PCP.PCPException;
import PCP.data.IPCPData;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;




/**
 *
 * @author gfurri20
 * @author Alessio789
 */
public class PCPMinInterpreter_Test
{
    @Test
    public void interpretRegistration() throws PCPException 
    {
        //expected
        Registration reg = new Registration("gfurri20", "general");
        ArrayList<byte[]> regBytes = new ArrayList<> (reg.toBytes());
        byte[] expected = regBytes.get(0);
        
        PCPMinInterpreter interpreter = new PCPMinInterpreter();
        
        //result
        Registration interpretedReg = (Registration) interpreter.interpret(regBytes.get(0));
        ArrayList<byte[]> interpretedRegBytes = new ArrayList<> (interpretedReg.toBytes());
        byte[] result = interpretedRegBytes.get(0);
        
        if ( regBytes.size() != 1 )
            Assert.fail();
               
        Assert.assertArrayEquals(expected, result) ;
    }
    
    @Test
    public void interpretDisconnection() throws PCPException
    {
        //expected
        Disconnection dis = new Disconnection( new byte[] {50,49} ); // id = (int) 21
        ArrayList<byte[]> disBytes = new ArrayList<> (dis.toBytes());
        byte[] expected = disBytes.get(0);
        
        PCPMinInterpreter interpreter = new PCPMinInterpreter();
        
        //result
        Disconnection interpretedDis = (Disconnection) interpreter.interpret(disBytes.get(0));
        ArrayList<byte[]> interpretedDisBytes = new ArrayList<> (interpretedDis.toBytes());
        byte[] result = interpretedDisBytes.get(0);
        
        if ( disBytes.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals(expected, result) ;
    }
    
    @Test
    public void interpretAliasChange() throws PCPException
    {
        //expected
        AliasChange ac = new AliasChange( new byte[] {50,49}, "gfurri" , "gfurri20" );
        ArrayList<byte[]> acBytes = new ArrayList<> (ac.toBytes());
        byte[] expected = acBytes.get(0);
        
        PCPMinInterpreter interpreter = new PCPMinInterpreter();
        
        //result
        AliasChange interpretedAc = (AliasChange) interpreter.interpret(acBytes.get(0));
        ArrayList<byte[]> interpretedAcBytes = new ArrayList<> (interpretedAc.toBytes());
        byte[] result = interpretedAcBytes.get(0);
        
        if ( acBytes.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals(expected, result) ;
    }
    
    @Test
    public void interpretGroupUserListRrq() throws PCPException 
    {
        //expected
        GroupUsersListRrq request = new GroupUsersListRrq( new byte[] {1, 2} );
        ArrayList<byte[]> requestBytes = new ArrayList<>( request.toBytes() );
        byte[] expected = requestBytes.get( 0 );
        
        PCPMinInterpreter interpreter = new PCPMinInterpreter();
        
        //result
        GroupUsersListRrq interpretedRequest = (GroupUsersListRrq) interpreter.interpret( requestBytes.get(0) );
        ArrayList<byte[]> interpretedRequestBytes = new ArrayList<> ( interpretedRequest.toBytes() );
        byte[] result = interpretedRequestBytes.get( 0 );
        
        if ( requestBytes.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void interpretMsgUserToGroup_single() throws PCPException
    {
        //expected
        Set<IPCPData> incompleteDataList = new HashSet<>();
        MsgUserToGroup singleMessage = new MsgUserToGroup( new byte[] {1, 2} , "test" );
        ArrayList<byte[]> singleMessageBytes = new ArrayList<>( singleMessage.toBytes() );
        byte[] expected = singleMessageBytes.get( 0 );
        
        PCPMinInterpreter interpreter = new PCPMinInterpreter( incompleteDataList );
        
        //result
        MsgUserToGroup interpretedSingleMessage = (MsgUserToGroup) interpreter.interpret( singleMessageBytes.get( 0 ) );
        ArrayList<byte[]> interpretedSingleMessageBytes = new ArrayList<> ( interpretedSingleMessage.toBytes() );
        byte[] result = interpretedSingleMessageBytes.get( 0 );
        
        if ( singleMessageBytes.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void interpretMsgUserToGroup_multiple() throws PCPException
    {
        //expected
        String message = "Verona prima città in Italia per controlli in ambito di immigrazione\n„\n Verona è prima in Italia per identificazioni, rilievi e denunce per violazioni delle norme sull’immigrazione effettuati dalla Polizia locale. È quanto è emerso, giovedì mattina, dall’assemblea Anci di Arezzo, alla quale ha partecipato anche il presidente della Repubblica Sergio Mattarella. Durante la mattinata è stato presentato l’ottavo Rapporto italiano sull’attività delle Polizie locali che illustra le attività svolte nel 2018 dai Corpi di 132 città, Comuni con più di 50mila abitanti.\n\n Verona, Padova e Roma sono prime perché insieme hanno realizzato il 75,9 % di tutte le verifiche portate a termine a livello nazionale, che in totale sono state 89.848. In 47 città non è stata effettuata alcuna identificazione.\n \n Il rapporto, curato da Anci, sottolinea che “nelle città più grandi confluiscono numerosi flussi migratori, ma la dimensione dei Comuni non è l’unica variabile da considerare. Tra i fattori che incidono sull’attività di controllo e identificazione le scelte politiche dettate dall’attenzione posta dagli amministratori sul tema”.\n \n Il primo anno intero della mia amministrazione ci porta alla ribalta nazionale su un tema a me caro come la sicurezza - dice il sindaco Federico Sboarina -. Siamo sul podio italiano insieme a Roma e Padova perché in queste tre città è stato fatto il 75% delle identificazioni sugli immigrati. È la conferma che le nostre non sono parole quando diciamo che il controllo del territorio si ottiene solo così: con gli agenti in strada tutti i giorni. La certificazione ci arriva dall'Anci che ha analizzato i dati delle Polizie locali delle 132 maggiori città italiane. E se il 2018 è andato così, l'anno in corso non è stato da meno, lo dimostra anche l'ultima operazione fatta insieme ai Carabinieri dove in un solo giorno sono stati sgomberati 24 abusivi da un edificio. La tranquillità dei veronesi per me è un bene primario, che garantisce anche il sano sviluppo imprenditoriale e per questo ringrazio il Prefetto che coordina il Comitato ordine pubblico e che è un interlocutore attento alle mie segnalazioni. A Verona c'è spazio per tutti, tranne per chi non rispetta le regole. E a queste persone, la mia amministrazione sta continuamente dimostrando che qui non sono le benvenute e che non facciamo sconti a nessuno. Ma mentre noi in ambito locale facciamo la nostra parte nell'interesse dei cittadini, quel che manca è l'altro tassello, quello nazionale degli strumenti di espulsione. Manca infatti una vera gestione dei fenomeni migratori sia nell'accesso sia nell'espulsione per chi non ne ha diritto o per chi vive di espedienti. L'ho sempre detto, a Verona tutti sono benvenuti purché siano regolari e non siano una minaccia per la nostra comunità.\n \n Da questo rapporto emerge l’importante lavoro fatto dai nostri agenti nel controllo capillare per garantire la sicurezza a tutti i cittadini – spiega l’assessore alla Sicurezza Daniele Polato -. Il lavoro paga e la regola è di non abbassare mai la guardia. Non mi stancherò di ripetere che a Verona non esistono zone franche, ecco perché abbiamo intensificato i controlli nelle aree più sensibili, ma anche nei quartieri e negli edifici abbandonati. E molto spesso i cittadini sono le nostre prime vedette, grazie alle loro segnalazioni riusciamo ad intervenire in tempo reale e a risolvere diverse problematiche. Con i nuovi agenti, potremo ulteriormente intensificare i controlli, potenziando le attività degli uffici periferici e arrivare quasi a una sorta di vigile di quartiere. Grazie a tutto il comando per il sacrificio giornaliero e mi auguro che velocemente arrivi anche una sede dignitosa, visto la vergogna dell’attuale situazione ereditata.\n \n“\n \n Potrebbe interessarti: https://www.veronasera.it/attualita/verona-immigrazione-controlli-italia-21-novembre-2019.html\n";
        
        Set<IPCPData> incompleteDataList = new HashSet<>();
        MsgUserToGroup multipleMessage = new MsgUserToGroup( new byte[] {1, 2} , message );
        ArrayList<byte[]> multipleMessageBytes = new ArrayList<>( multipleMessage.toBytes() );
        
        PCPMinInterpreter interpreter = new PCPMinInterpreter( incompleteDataList );
        
        //result
        MsgUserToGroup interpretedMultipleMessage = null;
        for ( byte[] data : multipleMessageBytes ) 
            interpretedMultipleMessage = (MsgUserToGroup) interpreter.interpret( data );
        ArrayList<byte[]> interpretedMultipleMessageBytes = new ArrayList<> ( interpretedMultipleMessage.toBytes() );
        
        if ( multipleMessageBytes.size() < 2 )
            Assert.fail();
        
        for ( int i = 0; i < multipleMessageBytes.size(); i++ ) 
            Assert.assertArrayEquals(multipleMessageBytes.get(i), interpretedMultipleMessageBytes.get(i));
    }
    
    @Test
    public void interpretMsgUserToUser_single() throws PCPException
    {
        //expected
        Set<IPCPData> incompleteDataList = new HashSet<>();
        MsgUserToUser singleMessage = new MsgUserToUser( new byte[] {1, 2}, "testing", "test" );
        ArrayList<byte[]> singleMessageBytes = new ArrayList<>( singleMessage.toBytes() );
        byte[] expected = singleMessageBytes.get( 0 );
        
        PCPMinInterpreter interpreter = new PCPMinInterpreter( incompleteDataList );
        
        //result
        MsgUserToUser interpretedSingleMessage = (MsgUserToUser) interpreter.interpret( singleMessageBytes.get( 0 ) );
        ArrayList<byte[]> interpretedSingleMessageBytes = new ArrayList<> ( interpretedSingleMessage.toBytes() );
        byte[] result = interpretedSingleMessageBytes.get( 0 );
        
        if ( singleMessageBytes.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void interpretMsgUserToUser_multiple() throws PCPException
    {
        //expected
        String message = "Verona prima città in Italia per controlli in ambito di immigrazione\n„\n Verona è prima in Italia per identificazioni, rilievi e denunce per violazioni delle norme sull’immigrazione effettuati dalla Polizia locale. È quanto è emerso, giovedì mattina, dall’assemblea Anci di Arezzo, alla quale ha partecipato anche il presidente della Repubblica Sergio Mattarella. Durante la mattinata è stato presentato l’ottavo Rapporto italiano sull’attività delle Polizie locali che illustra le attività svolte nel 2018 dai Corpi di 132 città, Comuni con più di 50mila abitanti.\n\n Verona, Padova e Roma sono prime perché insieme hanno realizzato il 75,9 % di tutte le verifiche portate a termine a livello nazionale, che in totale sono state 89.848. In 47 città non è stata effettuata alcuna identificazione.\n \n Il rapporto, curato da Anci, sottolinea che “nelle città più grandi confluiscono numerosi flussi migratori, ma la dimensione dei Comuni non è l’unica variabile da considerare. Tra i fattori che incidono sull’attività di controllo e identificazione le scelte politiche dettate dall’attenzione posta dagli amministratori sul tema”.\n \n Il primo anno intero della mia amministrazione ci porta alla ribalta nazionale su un tema a me caro come la sicurezza - dice il sindaco Federico Sboarina -. Siamo sul podio italiano insieme a Roma e Padova perché in queste tre città è stato fatto il 75% delle identificazioni sugli immigrati. È la conferma che le nostre non sono parole quando diciamo che il controllo del territorio si ottiene solo così: con gli agenti in strada tutti i giorni. La certificazione ci arriva dall'Anci che ha analizzato i dati delle Polizie locali delle 132 maggiori città italiane. E se il 2018 è andato così, l'anno in corso non è stato da meno, lo dimostra anche l'ultima operazione fatta insieme ai Carabinieri dove in un solo giorno sono stati sgomberati 24 abusivi da un edificio. La tranquillità dei veronesi per me è un bene primario, che garantisce anche il sano sviluppo imprenditoriale e per questo ringrazio il Prefetto che coordina il Comitato ordine pubblico e che è un interlocutore attento alle mie segnalazioni. A Verona c'è spazio per tutti, tranne per chi non rispetta le regole. E a queste persone, la mia amministrazione sta continuamente dimostrando che qui non sono le benvenute e che non facciamo sconti a nessuno. Ma mentre noi in ambito locale facciamo la nostra parte nell'interesse dei cittadini, quel che manca è l'altro tassello, quello nazionale degli strumenti di espulsione. Manca infatti una vera gestione dei fenomeni migratori sia nell'accesso sia nell'espulsione per chi non ne ha diritto o per chi vive di espedienti. L'ho sempre detto, a Verona tutti sono benvenuti purché siano regolari e non siano una minaccia per la nostra comunità.\n \n Da questo rapporto emerge l’importante lavoro fatto dai nostri agenti nel controllo capillare per garantire la sicurezza a tutti i cittadini – spiega l’assessore alla Sicurezza Daniele Polato -. Il lavoro paga e la regola è di non abbassare mai la guardia. Non mi stancherò di ripetere che a Verona non esistono zone franche, ecco perché abbiamo intensificato i controlli nelle aree più sensibili, ma anche nei quartieri e negli edifici abbandonati. E molto spesso i cittadini sono le nostre prime vedette, grazie alle loro segnalazioni riusciamo ad intervenire in tempo reale e a risolvere diverse problematiche. Con i nuovi agenti, potremo ulteriormente intensificare i controlli, potenziando le attività degli uffici periferici e arrivare quasi a una sorta di vigile di quartiere. Grazie a tutto il comando per il sacrificio giornaliero e mi auguro che velocemente arrivi anche una sede dignitosa, visto la vergogna dell’attuale situazione ereditata.\n \n“\n \n Potrebbe interessarti: https://www.veronasera.it/attualita/verona-immigrazione-controlli-italia-21-novembre-2019.html\n";
        
        Set<IPCPData> incompleteDataList = new HashSet<>();
        MsgUserToUser multipleMessage = new MsgUserToUser( new byte[] {1, 2} , "testing", message );
        ArrayList<byte[]> multipleMessageBytes = new ArrayList<>( multipleMessage.toBytes() );
        
        PCPMinInterpreter interpreter = new PCPMinInterpreter( incompleteDataList );
        
        //result
        MsgUserToUser interpretedMultipleMessage = null;
        for ( byte[] data : multipleMessageBytes ) 
            interpretedMultipleMessage = (MsgUserToUser) interpreter.interpret( data );
        ArrayList<byte[]> interpretedMultipleMessageBytes = new ArrayList<> ( interpretedMultipleMessage.toBytes() );
        
        if ( multipleMessageBytes.size() < 2 )
            Assert.fail();
        
        for ( int i = 0; i < multipleMessageBytes.size(); i++ ) 
            Assert.assertArrayEquals(multipleMessageBytes.get(i), interpretedMultipleMessageBytes.get(i));
    }
    
    @Test
    public void interpretErrorMsg() throws PCPException
    {
        //expected
        ErrorMsg err = new ErrorMsg( PCPException.ErrorCode.PackageMalformed );
        ArrayList<byte[]> errBytes = new ArrayList<> (err.toBytes());
        byte[] expected = errBytes.get(0);
        
        PCPMinInterpreter interpreter = new PCPMinInterpreter();
        
        //result
        ErrorMsg interpretedErr = (ErrorMsg) interpreter.interpret(errBytes.get(0));
        ArrayList<byte[]> interpretedErrBytes = new ArrayList<> (interpretedErr.toBytes());
        byte[] result = interpretedErrBytes.get(0);
        
        if ( errBytes.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals(expected, result) ;
    }
}
