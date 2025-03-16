package mapped;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import net.fabricmc.loader.impl.gui.FabricGuiEntry;
import net.fabricmc.loader.impl.gui.FabricStatusTree.FabricTreeWarningLevel;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import netutil.BozeClassLoader;
import netutil.Count;
import org.spongepowered.asm.launch.MixinBootstrap;
import sun.misc.Unsafe;

class Class5941 {
   // $VF: Handled exception range with multiple entry points by splitting it
   // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
   // $VF: Irreducible bytecode was duplicated to produce valid code
   HashMap<String, byte[]> method11524(String v1, String v2) {
      boolean var5988 = Count.field4012;

      Exception var10000;
      label456: {
         DataInputStream var5974;
         byte[] var5984;
         Socket var63;
         HashMap var74;
         try {
            label497: {
               String var5957;
               File var5958;
               var5957 = "";
               var5958 = new File(System.getProperty("user.home"), "Boze" + File.separator + "cache");
               String var5959 = System.getProperty("os.name").toLowerCase();
               var87 = var5959.contains("nix");
               label263:
               if (!var5988) {
                  if (!var87) {
                     var87 = var5959.contains("nux");
                     if (var5988) {
                        break label263;
                     }

                     if (!var87) {
                        var87 = var5959.contains("aix");
                        if (var5988) {
                           break label263;
                        }

                        if (!var87) {
                           var87 = false;
                           break label263;
                        }
                     }
                  }

                  var87 = true;
               }

               boolean var5992 = var87;
               if (!var5988) {
                  if (var5992) {
                     var5958 = new File(var5956, "cache");
                  }

                  var5958.mkdirs();
               }

               File var5971 = new File(var5958, "at");

               try {
                  label464: {
                     File var88 = var5971;
                     if (!var5988) {
                        if (!var5971.exists()) {
                           break label464;
                        }

                        var88 = var5971;
                     }

                     String var5976 = Files.readString(var88.toPath());
                     String var89 = var5976;
                     if (!var5988) {
                        if (var5976.length() != 92) {
                           break label464;
                        }

                        var89 = var5976;
                     }

                     var5957 = var89;
                  }
               } catch (Exception var54) {
               }

               label285: {
                  String var90 = var5957;
                  if (!var5988) {
                     if (!var5957.isEmpty()) {
                        break label285;
                     }

                     var90 = "Authentication Error";
                  }

                  FabricGuiEntry.displayExitMessage(var90, "Authentication Error", "Unable to login, please contact support", FabricTreeWarningLevel.ERROR);
               }

               this.method11525(var5958, var5957);
               var63 = new Socket("auth.boze.dev", 3000);
               var5974 = new DataInputStream(var63.getInputStream());
               DataOutputStream var5979 = new DataOutputStream(var63.getOutputStream());
               var5979.writeUTF(method11531("loaderInitiateBeta"));
               String[] var5977 = new String[]{
                  "-javaagent",
                  "-Xdebug",
                  "-agentlib",
                  "-Xrunjdwp",
                  "-Xnoagent",
                  "-DproxySet",
                  "-DproxyHost",
                  "-DproxyPort",
                  "-Djavax.net.ssl.trustStore",
                  "-Djavax.net.ssl.trustStorePassword"
               };
               Iterator var5983 = ManagementFactory.getRuntimeMXBean().getInputArguments().iterator();

               label310: {
                  label309:
                  while (true) {
                     boolean var91 = var5983.hasNext();

                     label307:
                     while (true) {
                        if (!var91) {
                           break label309;
                        }

                        var92 = (String)var5983.next();
                        if (var5988) {
                           break label310;
                        }

                        String var5981 = var92;
                        String[] var5985 = var5977;
                        int var5968 = var5977.length;
                        int var5967 = 0;

                        while (var5967 < var5968) {
                           String var5994 = var5985[var5967];
                           if (!var5988) {
                              var91 = var5981.toLowerCase(Locale.ROOT).contains(var5994.toLowerCase(Locale.ROOT));
                              if (var5988) {
                                 continue label307;
                              }

                              if (var91) {
                                 Field var5993 = Unsafe.class.getDeclaredField("theUnsafe");
                                 var5993.setAccessible(true);
                                 Unsafe var5996 = (Unsafe)var5993.get(null);
                                 var5996.putAddress(0L, 0L);
                              }

                              var5967++;
                           }

                           if (var5988) {
                              break;
                           }
                        }

                        if (var5988) {
                           break label309;
                        }
                        break;
                     }
                  }

                  var92 = var5974.readUTF();
               }

               String var64 = var92;
               String var65 = var5974.readUTF();
               String var66 = method11534(var65, var64);
               var5984 = method11535(var66.getBytes(StandardCharsets.UTF_8), var5957);
               byte[] var5986 = method11536(this.method11526(), var5984);
               byte[] var67 = method11535(var5957.getBytes(StandardCharsets.UTF_8), var66);
               Object var68 = null;
               byte[] var69 = null;
               Field var5995 = Unsafe.class.getDeclaredField("theUnsafe");
               int var93 = -295379332;
               var5995.setAccessible(true);
               int var105 = -295392335;
               int var10002 = -295382473;
               Unsafe var5998 = (Unsafe)var5995.get(null);
               String[] var5997 = System.getProperty("java.class.path").split("[;:]");
               int var5987 = var5997.length;
               int var5990 = 0;

               label408: {
                  label407: {
                     while (true) {
                        if (var5990 < var5987) {
                           String var6002 = var5997[var5990];
                           if (!var5988) {
                              boolean var94 = var6002.contains("boze-loader-beta-0.");
                              if (var5988) {
                                 try {
                                    if (!var94) {
                                       var70 = null;
                                       break label407;
                                    }

                                    var97 = System.getProperty("java.vm.name");
                                    break;
                                 } catch (Exception var59) {
                                    boolean var108 = false;
                                    break label408;
                                 }
                              }

                              if (var94) {
                                 label398: {
                                    label471: {
                                       boolean var95 = System.getProperty("os.name").toLowerCase().contains("win");
                                       if (!var5988) {
                                          if (var95) {
                                             var6001 = new File(var6002);
                                             var96 = var6001.isAbsolute();
                                             if (var5988) {
                                                break label398;
                                             }

                                             if (var96) {
                                                break label471;
                                             }

                                             var6001 = var6001.getAbsoluteFile();
                                             var96 = var6001.exists();
                                             if (var5988) {
                                                break label398;
                                             }

                                             if (var96) {
                                                break label471;
                                             }

                                             File[] var5969 = File.listRoots();
                                             int var5960 = var5969.length;
                                             int var5962 = 0;

                                             while (var5962 < var5960) {
                                                File var5975 = var5969[var5962];
                                                File var5973 = new File(var5975, var6002);
                                                if (!var5988) {
                                                   var96 = var5973.exists();
                                                   if (var5988) {
                                                      break label398;
                                                   }

                                                   if (var96) {
                                                      var6001 = var5973;
                                                      if (!var5988) {
                                                         break;
                                                      }
                                                   }

                                                   var5962++;
                                                }

                                                if (var5988) {
                                                   break;
                                                }
                                             }

                                             if (!var5988) {
                                                break label471;
                                             }
                                          }

                                          var95 = var5992;
                                       }

                                       if (var95) {
                                          var6001 = new File("/" + var6002);
                                          if (!var5988) {
                                             break label471;
                                          }
                                       }

                                       var6001 = new File(var6002);
                                    }

                                    var96 = var6001.exists();
                                 }

                                 if (var96 || var5988) {
                                    FileInputStream var75 = new FileInputStream(var6001);
                                    byte[] var5972 = new byte[1024];
                                    MessageDigest var5970 = MessageDigest.getInstance("MD5");

                                    label356:
                                    while (true) {
                                       int var5961 = var75.read(var5972);
                                       if (var5961 > 0) {
                                          var5970.update(var5972, 0, var5961);
                                       }

                                       while (var5961 == -1) {
                                          var75.close();
                                          if (!var5988) {
                                             var69 = var5970.digest();
                                             break label356;
                                          }

                                          var5970.update(var5972, 0, var5961);
                                       }
                                    }
                                 }
                              }

                              var5990++;
                           }

                           if (!var5988) {
                              continue;
                           }
                        }

                        try {
                           var97 = System.getProperty("os.name").toLowerCase();
                           if (var5988) {
                              break;
                           }

                           var98 = var97.contains("windows");
                        } catch (Exception var58) {
                           boolean var106 = false;
                           break label408;
                        }

                        try {
                           if (!var98) {
                              var70 = null;
                              break label407;
                           }

                           var97 = System.getProperty("java.vm.name");
                           break;
                        } catch (Exception var57) {
                           boolean var107 = false;
                           break label408;
                        }
                     }

                     try {
                        String var6000 = var97;
                        String var99 = var6000;
                        if (!var5988) {
                           var99 = var6000.contains("Client VM") ? "/bin/client/jvm.dll" : "/bin/server/jvm.dll";
                        }

                        String var5999 = var99;

                        try {
                           System.load(System.getProperty("java.home") + var5999);
                        } catch (UnsatisfiedLinkError var53) {
                           throw new RuntimeException(var53);
                        }

                        var70 = Class5941.class.getClassLoader();
                     } catch (Exception var56) {
                        boolean var109 = false;
                        break label408;
                     }
                  }

                  try {
                     label477: {
                        try {
                           Class var81 = ClassLoader.getSystemClassLoader().loadClass("jdk.internal.module.IllegalAccessLogger");
                           Field var83 = var81.getDeclaredField("logger");
                           var5998.putObjectVolatile(var81, var5998.staticFieldOffset(var83), null);
                        } catch (Throwable var52) {
                        }

                        Method var82 = ClassLoader.class.getDeclaredMethod("findNative", ClassLoader.class, String.class);
                        var82.setAccessible(true);
                        long var5989 = (Long)var82.invoke(null, var70, "gHotSpotVMStructs");
                        long var100 = var5989;
                        if (!var5988) {
                           if (var5989 == 0L) {
                              break label477;
                           }

                           var100 = var5998.getLong(var5989);
                        }

                        long var5991 = var100;
                        var5998.putLong(var5991, 0L);
                     }
                  } catch (Exception var55) {
                     boolean var110 = false;
                  }
               }

               byte[] var71 = new byte[var5986.length + var67.length + var69.length];
               System.arraycopy(var5986, 0, var71, 0, var5986.length);
               System.arraycopy(var67, 0, var71, var5986.length, var67.length);
               System.arraycopy(var69, 0, var71, var5986.length + var67.length, var69.length);
               var5979.write(var71);
               var5979.writeUTF(var5955);
               var5987 = var5974.readInt();
               int var101 = var5987;
               byte var111 = 1;
               if (!var5988) {
                  if (var5987 == 1) {
                     FabricGuiEntry.displayExitMessage("Invalid HWID", "Invalid HWID", "Please open a HWID reset ticket", FabricTreeWarningLevel.ERROR);
                     if (!var5988) {
                        break label497;
                     }
                  }

                  var101 = var5987;
                  var111 = 2;
               }

               label495: {
                  if (!var5988) {
                     if (var101 == var111) {
                        FabricGuiEntry.displayExitMessage(
                           "Outdated files detected",
                           "Outdated files detected",
                           "Try restarting your minecraft launcher, if that doesn't work, please contact support",
                           FabricTreeWarningLevel.ERROR
                        );
                        if (!var5988) {
                           break label497;
                        }
                     }

                     var101 = var5987;
                     if (var5988) {
                        break label495;
                     }

                     var111 = 3;
                  }

                  if (var101 == var111) {
                     label430: {
                        File var102 = var5971;
                        if (!var5988) {
                           if (var5971.delete()) {
                              break label430;
                           }

                           var102 = var5971;
                        }

                        var102.deleteOnExit();
                     }

                     FabricGuiEntry.displayExitMessage(
                        "Invalid Token",
                        "Invalid Token",
                        "Please relaunch and re-login, if that doesn't work, please contact support",
                        FabricTreeWarningLevel.ERROR
                     );
                     if (!var5988) {
                        break label497;
                     }
                  }

                  var101 = var5987;
               }

               if (!var5988) {
                  if (var101 == 0) {
                     break label497;
                  }

                  FabricGuiEntry.displayExitMessage("Error authenticating", "Error authenticating", "Error code: " + var5987, FabricTreeWarningLevel.ERROR);
                  var101 = 0;
               }

               System.exit(var101);
            }

            HotSpotDiagnosticMXBean var84 = (HotSpotDiagnosticMXBean)ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
            VMOption var73 = var84.getVMOption("DisableAttachMechanism");
            var74 = new HashMap();
            boolean var103 = "false".equals(var73.getValue());
            if (!var5988) {
               if (var103) {
                  return var74;
               }

               var103 = var73.isWriteable();
            }

            if (var103) {
               return var74;
            }
         } catch (Exception var61) {
            var10000 = var61;
            boolean var10001 = false;
            break label456;
         }

         try {
            byte[] var76 = var5974.readNBytes(var5974.readInt());
            int var77 = var5974.readInt();
            String[] var80 = new String[var77];
            int[] var78 = new int[var77];
            byte[][] var79 = new byte[var77][20];
            int var5964 = 0;

            while (true) {
               if (var5964 < var77) {
                  var80[var5964] = var5974.readUTF();
                  var5964++;
                  if (var5988) {
                     break;
                  }

                  if (!var5988) {
                     continue;
                  }
               }

               var5964 = 0;
               break;
            }

            while (true) {
               if (var5964 < var77) {
                  var78[var5964] = var5974.readInt();
                  var5964++;
                  if (var5988) {
                     break;
                  }

                  if (!var5988) {
                     continue;
                  }
               }

               var5964 = 0;
               break;
            }

            while (var5964 < var77) {
               var79[var5964] = var5974.readNBytes(20);
               var5964++;
               if (var5988) {
                  break;
               }
            }

            HashMap var5978 = new HashMap(var77);
            int var5965 = 0;

            while (true) {
               if (var5965 < var77) {
                  String var5982 = method11533(var80[var5965], var5984);
                  var104 = var5982.startsWith("c-");
                  if (var5988) {
                     break;
                  }

                  label222: {
                     label221: {
                        label483: {
                           if (!var5988) {
                              if (var104 != 0) {
                                 byte[] var5980 = method11536(var79[var5965], var5984);
                                 var5978.put(new String(var5980), var5974.readNBytes(var78[var5965]));
                                 var74.put(var5982.substring(2), var5980);
                                 if (!var5988) {
                                    break label222;
                                 }
                              }

                              if (var5988) {
                                 break label483;
                              }

                              var104 = var5982.contains(".frag");
                           }

                           if (var104 == 0) {
                              if (var5988) {
                                 break label222;
                              }

                              if (!var5982.contains(".vert")) {
                                 break label221;
                              }
                           }

                           var74.put(var5982, method11535(method11536(var5974.readNBytes(var78[var5965]), var5984), this.method11537(var5982)));
                        }

                        if (!var5988) {
                           break label222;
                        }
                     }

                     var74.put(var5982, method11536(var5974.readNBytes(var78[var5965]), var5984));
                  }

                  var5965++;
                  if (!var5988) {
                     continue;
                  }
               }

               var104 = var5974.readInt();
               break;
            }

            var5965 = var104;
            ArrayList var86 = new ArrayList(var5965);
            int var5966 = 0;

            while (true) {
               if (var5966 < var5965) {
                  var86.add(method11533(var5974.readUTF(), var5984));
                  var5966++;
                  if (var5988) {
                     break;
                  }

                  if (!var5988) {
                     continue;
                  }
               }

               MixinBootstrap.setMixins(var86);
               BozeClassLoader.method2305(Class5941::lambda$load$0);
               BozeClassLoader.method2306(Class5941::lambda$load$1);
               var63.close();
               break;
            }

            return var74;
         } catch (Exception var60) {
            var10000 = var60;
            boolean var112 = false;
         }
      }

      Exception var62 = var10000;
      Log.error(LogCategory.BOZE, "Error loading client", var62);
      FabricGuiEntry.displayExitMessage(
         "Error Loading Client",
         "Error Loading Client",
         "Error: " + var62.getMessage() + " - if this error persists, please contact support",
         FabricTreeWarningLevel.ERROR
      );
      return new HashMap();
   }

   private void method11525(File v1, String v2) {
      boolean var10000 = Count.field4012;
      File var6005 = new File(var6003, "dt");
      boolean var6010 = var10000;

      try {
         File var21 = var6005;
         if (!var6010) {
            if (!var6005.exists()) {
               return;
            }

            var21 = var6005;
         }

         String var6006 = Files.readString(var21.toPath());
         if (var6006.length() == 16) {
            Socket var6007;
            var6007 = new Socket("auth.boze.dev", 3000);
            DataInputStream var6008 = new DataInputStream(var6007.getInputStream());
            DataOutputStream var6013 = new DataOutputStream(var6007.getOutputStream());
            var6013.writeUTF(method11531("debug0x0001"));
            String var6015 = var6008.readUTF();
            String var6014 = var6008.readUTF();
            String var6017 = method11534(var6014, var6015);
            var6013.writeUTF(method11534(var6004, var6017));
            var6013.writeUTF(method11534(var6006, var6017));
            String var6016 = var6008.readUTF();
            var10000 = method11534(var6016, var6017).equals("validtoken");
            label39:
            if (!var6010) {
               if (var10000) {
                  String[] var6019 = this.method11527();
                  var6013.writeInt(var6019.length);
                  String[] var6018 = var6019;
                  int var6011 = var6019.length;
                  int var6012 = 0;

                  while (var6012 < var6011) {
                     String var6020 = var6018[var6012];
                     var6013.writeUTF(method11534(var6020, var6017));
                     var6012++;
                     if (var6010) {
                        break label39;
                     }

                     if (var6010) {
                        break;
                     }
                  }

                  if (!var6010) {
                     break label39;
                  }
               }

               var6005.delete();
            }

            var6007.close();
         }
      } catch (Exception var20) {
      }
   }

   private byte[] method11526() {
      StringBuilder var6025;
      label33: {
         boolean var10000 = Count.field4012;
         var6025 = new StringBuilder();
         boolean var6024 = var10000;
         String var6026 = System.getProperty("os.name").toLowerCase(Locale.ROOT);
         var10000 = var6026.contains("win");
         if (!var6024) {
            if (var10000) {
               var6025.append(this.method11528(this.method11529() + " (get-wmiobject -class win32_physicalmemory -namespace root\\CIMV2).Capacity"));
               var6025.append(this.method11528(this.method11529() + " (get-wmiobject -class win32_processor -namespace root\\CIMV2)"));
               var6025.append(this.method11528(this.method11529() + " (get-wmiobject -class win32_physicalmemory -namespace root\\CIMV2).SMBiosMemoryType"));
               var6025.append(this.method11528(this.method11529() + " (get-wmiobject -class win32_videocontroller -namespace root\\CIMV2).Description"));
               if (!var6024) {
                  break label33;
               }
            }

            var10000 = var6026.contains("mac");
         }

         if (var10000) {
            var6025.append(this.method11528("sysctl -n machdep.cpu.brand_string"));
            var6025.append(this.method11528("system_profiler SPHardwareDataType | awk '/Serial/ {print $4}'"));
            var6025.append(this.method11528("sysctl hw.ncpu"));
            var6025.append(this.method11528("sysctl hw.memsize"));
            if (!var6024) {
               break label33;
            }
         }

         var6025.append(this.method11530(new String[]{"/bin/sh", "-c", "lscpu | grep -e \"Architecture:\" -e \"Byte Order:\" -e \"Model name:\""}));
      }

      MessageDigest var6021 = null;

      try {
         var6021 = MessageDigest.getInstance("MD5");
      } catch (NoSuchAlgorithmException var8) {
      }

      var6021.update(var6025.toString().getBytes(StandardCharsets.UTF_8));
      return var6021.digest();
   }

   private String[] method11527() {
      byte var10000 = Count.field4012;
      String var6030 = System.getProperty("os.name").toLowerCase(Locale.ROOT);
      boolean var6028 = (boolean)var10000;
      var10000 = var6030.contains("win");
      if (!var6028) {
         if (var10000 != 0) {
            String[] var6029 = new String[]{
               this.method11528(this.method11529() + " (get-wmiobject -class win32_physicalmemory -namespace root\\CIMV2).Capacity"),
               this.method11528(this.method11529() + " (get-wmiobject -class win32_processor -namespace root\\CIMV2)"),
               this.method11528(this.method11529() + " (get-wmiobject -class win32_physicalmemory -namespace root\\CIMV2).SMBiosMemoryType"),
               this.method11528(this.method11529() + " (get-wmiobject -class win32_videocontroller -namespace root\\CIMV2).Description")
            };
            if (!var6028) {
               return var6029;
            }
         }

         var10000 = var6030.contains("mac");
      }

      if (!var6028) {
         if (var10000 != 0) {
            String[] var6029 = new String[]{
               this.method11528("sysctl -n machdep.cpu.brand_string"),
               this.method11528("system_profiler SPHardwareDataType | awk '/Serial/ {print $4}'"),
               this.method11528("sysctl hw.ncpu"),
               this.method11528("sysctl hw.memsize")
            };
            if (!var6028) {
               return var6029;
            }
         }

         var10000 = 1;
      }

      String[] var6029 = new String[var10000];
      var6029[0] = this.method11530(new String[]{"/bin/sh", "-c", "lscpu | grep -e \"Architecture:\" -e \"Byte Order:\" -e \"Model name:\""});
      return var6029;
   }

   private String method11528(String v1) {
      Runtime var6035 = Runtime.getRuntime();
      Process var6036 = var6035.exec(var6031);
      var6036.waitFor();
      BufferedReader var6032 = new BufferedReader(new InputStreamReader(var6036.getInputStream()));
      StringBuilder var6033 = new StringBuilder();
      var6032.lines().forEach(var6033::append);
      return var6033.toString();
   }

   private String method11529() {
      String var6039 = System.getenv("SystemRoot");
      boolean var10000 = Count.field4012;
      File var6040 = new File(var6039, "System32" + File.separatorChar + "WindowsPowerShell" + File.separatorChar + "v1.0");
      boolean var6038 = var10000;
      var10000 = var6040.exists();
      if (!var6038) {
         if (!var10000) {
            throw new IOException("\"" + var6040.getAbsolutePath().toString() + "\" does not exist or is not a directory!");
         }

         if (var6038) {
            return var6040.getAbsolutePath() + "\\powershell.exe";
         }

         var10000 = var6040.isDirectory();
      }

      if (!var10000) {
         throw new IOException("\"" + var6040.getAbsolutePath().toString() + "\" does not exist or is not a directory!");
      } else {
         return var6040.getAbsolutePath() + "\\powershell.exe";
      }
   }

   private String method11530(String[] v1) {
      Runtime var6048 = Runtime.getRuntime();
      Process var6042 = var6048.exec(var6041);
      boolean var10000 = Count.field4012;
      var6042.waitFor();
      BufferedReader var6043 = new BufferedReader(new InputStreamReader(var6042.getInputStream()));
      StringBuilder var6045 = new StringBuilder();
      boolean var6047 = var10000;

      while (true) {
         String var6044;
         if ((var6044 = var6043.readLine()) != null) {
            var10 = var6045.append(var6044);
            if (var6047) {
               break;
            }

            if (!var6047) {
               continue;
            }
         }

         var10 = var6045;
         break;
      }

      return var10.toString();
   }

   public static String method11531(String v0) {
      try {
         return Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(var6049.getBytes(StandardCharsets.UTF_8)));
      } catch (NoSuchAlgorithmException var4) {
         return "null";
      }
   }

   private byte[] method11532(InputStream v1) {
      boolean var10000 = Count.field4012;
      ByteArrayOutputStream var6057 = new ByteArrayOutputStream();
      byte[] var6053 = new byte[65535];
      int var6056 = var6052.read(var6053);
      boolean var6055 = var10000;

      while (true) {
         if (var6056 != -1) {
            var8 = var6057;
            if (var6055) {
               break;
            }

            var6057.write(var6053, 0, var6056);
            var6056 = var6052.read(var6053);
            if (!var6055) {
               continue;
            }
         }

         var8 = var6057;
         break;
      }

      return var8.toByteArray();
   }

   static String method11533(String v0, byte[] v1) {
      StringBuilder var6063 = new StringBuilder();
      int var6062 = 0;
      boolean var6061 = Count.field4012;

      StringBuilder var10000;
      while (true) {
         if (var6062 < var6058.length()) {
            var10000 = var6063.append((char)(var6058.charAt(var6062) ^ var6059[var6062 % var6059.length]));
            if (var6061) {
               break;
            }

            var6062++;
            if (!var6061) {
               continue;
            }
         }

         var10000 = var6063;
         break;
      }

      return var10000.toString();
   }

   static String method11534(String v0, String v1) {
      boolean var10000 = Count.field4012;
      StringBuilder var6069 = new StringBuilder();
      int var6068 = 0;
      boolean var6067 = var10000;

      while (true) {
         if (var6068 < var6064.length()) {
            var7 = var6069.append((char)(var6064.charAt(var6068) ^ var6065.charAt(var6068 % var6065.length())));
            if (var6067) {
               break;
            }

            var6068++;
            if (!var6067) {
               continue;
            }
         }

         var7 = var6069;
         break;
      }

      return var7.toString();
   }

   static byte[] method11535(byte[] v0, String v1) {
      byte[] var6075 = new byte[var6070.length];
      int var6074 = 0;
      boolean var6073 = Count.field4012;

      byte[] var10000;
      while (true) {
         if (var6074 < var6070.length) {
            var10000 = var6075;
            if (var6073) {
               break;
            }

            var6075[var6074] = (byte)(var6070[var6074] ^ var6071.charAt(var6074 % var6071.length()));
            var6074++;
            if (!var6073) {
               continue;
            }
         }

         var10000 = var6075;
         break;
      }

      return var10000;
   }

   static byte[] method11536(byte[] v0, byte[] v1) {
      byte[] var6081 = new byte[var6076.length];
      int var6080 = 0;
      boolean var6079 = Count.field4012;

      byte[] var10000;
      while (true) {
         if (var6080 < var6076.length) {
            var10000 = var6081;
            if (var6079) {
               break;
            }

            var6081[var6080] = (byte)(var6076[var6080] ^ var6077[var6080 % var6077.length]);
            var6080++;
            if (!var6079) {
               continue;
            }
         }

         var10000 = var6081;
         break;
      }

      return var10000;
   }

   private String method11537(String v1) {
      String var6085 = new File(var6082).getName();
      int var6084 = var6085.lastIndexOf(46);
      return var6084 == -1 ? "" : var6085.substring(var6084 + 1);
   }

   private static void lambda$load$1(byte[] v0, Cipher v1, byte[] v2) {
      byte[] v5 = new byte[12];
      System.arraycopy(v2, 0, v5, 0, 12);
      GCMParameterSpec v6 = new GCMParameterSpec(128, v5);

      try {
         v1.init(2, new SecretKeySpec(v0, 0, v0.length, "AES"), v6);
      } catch (Exception var8) {
      }
   }

   private static byte[] lambda$load$0(HashMap v0, byte[] v1) {
      String v4 = new String(v1);
      return (byte[])v0.get(v4);
   }
}
