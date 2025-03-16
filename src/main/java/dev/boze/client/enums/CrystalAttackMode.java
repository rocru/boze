package dev.boze.client.enums;

public enum CrystalAttackMode {
   Ignore,
   Off,
   Sequential,
   SeqStrict,
   Strict;

   private static final CrystalAttackMode[] field10 = method5();

   private static CrystalAttackMode[] method5() {
      return new CrystalAttackMode[]{Ignore, Off, Sequential, SeqStrict, Strict};
   }
}
