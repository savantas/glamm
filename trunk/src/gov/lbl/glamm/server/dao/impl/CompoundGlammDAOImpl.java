package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.CompoundDAO;
import gov.lbl.glamm.shared.GlammUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CompoundGlammDAOImpl implements CompoundDAO {
	

	public static final String GLOBAL_MAP_CPD_IDS				=
		"\"C00119\",\"C00118\",\"C00117\",\"C00116\",\"C00114\",\"C07075\",\"C04454\",\"C00504\",\"C09704\",\"C06442\",\"C00501\",\"C00111\",\"C00112\"," +
		"\"C00110\",\"C00508\",\"C03794\",\"C00509\",\"C00506\",\"C00106\",\"C00109\",\"C00108\",\"C03393\",\"C00103\",\"C00105\",\"C07085\",\"C07084\"," +
		"\"C07083\",\"C04442\",\"C07089\",\"C07087\",\"C07086\",\"C00514\",\"C00515\",\"C07092\",\"C00512\",\"C00100\",\"C00101\",\"C00519\",\"C00137\"," +
		"\"C00136\",\"C00135\",\"C04432\",\"C00522\",\"C00521\",\"C16482\",\"C00527\",\"C16480\",\"C00524\",\"C06462\",\"C00130\",\"C00133\",\"C00134\"," +
		"\"C00131\",\"C00132\",\"C03771\",\"C00124\",\"C00129\",\"C00128\",\"C04421\",\"C00530\",\"C03373\",\"C03372\",\"C00532\",\"C00534\",\"C00535\"," +
		"\"C06473\",\"C00120\",\"C00122\",\"C00123\",\"C06408\",\"C06407\",\"C06406\",\"C05807\",\"C04411\",\"C12126\",\"C04092\",\"C06416\",\"C02686\"," +
		"\"C16479\",\"C16474\",\"C16473\",\"C16476\",\"C11554\",\"C04405\",\"C12137\",\"C04409\",\"C07097\",\"C12144\",\"C07099\",\"C12145\",\"C07093\"," +
		"\"C07094\",\"C07095\",\"C06427\",\"C04076\",\"C06426\",\"C05817\",\"C05818\",\"C04063\",\"C03325\",\"C11522\",\"C00588\",\"C15789\",\"C16424\"," +
		"\"C05189\",\"C16428\",\"C15786\",\"C15787\",\"C00584\",\"C15785\",\"C00581\",\"C15780\",\"C15781\",\"C05174\",\"C15778\",\"C03319\",\"C15777\"," +
		"\"C05175\",\"C05172\",\"C13624\",\"C05178\",\"C05179\",\"C05176\",\"C01595\",\"C13629\",\"C14749\",\"C14748\",\"C01598\",\"C00596\",\"C15776\"," +
		"\"C00593\",\"C00590\",\"C03309\",\"C15767\",\"C13632\",\"C11543\",\"C11542\",\"C11545\",\"C13636\",\"C14771\",\"C14770\",\"C14773\",\"C14772\"," +
		"\"C14775\",\"C14774\",\"C14776\",\"C11540\",\"C11538\",\"C11539\",\"C14768\",\"C14769\",\"C08614\",\"C00547\",\"C01107\",\"C01103\",\"C00544\"," +
		"\"C01100\",\"C00540\",\"C01101\",\"C01102\",\"C06893\",\"C06892\",\"C04896\",\"C04895\",\"C08601\",\"C00559\",\"C00558\",\"C00550\",\"C04488\"," +
		"\"C11508\",\"C14315\",\"C00568\",\"C03345\",\"C04477\",\"C03344\",\"C04478\",\"C04475\",\"C03340\",\"C04874\",\"C15799\",\"C00577\",\"C05194\"," +
		"\"C05193\",\"C05191\",\"C05190\",\"C04468\",\"C15792\",\"C00570\",\"C15791\",\"C15794\",\"C15793\",\"C00573\",\"C15798\",\"C04462\",\"C15790\"," +
		"\"C01514\",\"C01134\",\"C01137\",\"C01921\",\"C04851\",\"C05212\",\"G13045\",\"C01528\",\"G13046\",\"C16143\",\"G13044\",\"C01146\",\"C01144\"," +
		"\"C01143\",\"C15700\",\"C05223\",\"C05220\",\"C01112\",\"C01118\",\"C01943\",\"C01944\",\"C17938\",\"C17937\",\"C01115\",\"C08586\",\"C07480\"," +
		"\"C07481\",\"C04823\",\"C04824\",\"C01124\",\"C07478\",\"C07479\",\"C01542\",\"C08590\",\"C05202\",\"C01172\",\"G00093\",\"C01176\",\"G00095\"," +
		"\"G00094\",\"G00097\",\"C01179\",\"G00099\",\"G00098\",\"C01170\",\"C05258\",\"C14781\",\"C14782\",\"C05259\",\"G11040\",\"C01185\",\"C01189\"," +
		"\"C01187\",\"C04807\",\"C01182\",\"C01180\",\"C05269\",\"C05268\",\"C05267\",\"C05266\",\"C05265\",\"C01561\",\"C05264\",\"C01953\",\"C05263\"," +
		"\"C05262\",\"C05261\",\"C05260\",\"C08579\",\"C01157\",\"C02282\",\"C02280\",\"G00077\",\"G00078\",\"G00063\",\"G00062\",\"C01165\",\"C01163\"," +
		"\"G00060\",\"C01164\",\"C01161\",\"C02291\",\"G00067\",\"G00066\",\"C05247\",\"C00180\",\"C00181\",\"C16829\",\"C00184\",\"C00183\",\"C00188\"," +
		"\"C00189\",\"C02642\",\"C00186\",\"C00187\",\"G00056\",\"G00057\",\"G00054\",\"C02646\",\"G00055\",\"C03722\",\"C02647\",\"G00058\",\"G00059\"," +
		"\"G00052\",\"G00050\",\"C05840\",\"C05842\",\"C05843\",\"C00190\",\"C00191\",\"C00194\",\"C00195\",\"C00197\",\"C00198\",\"C00199\",\"G00043\"," +
		"\"C02656\",\"G00044\",\"G00045\",\"C03715\",\"G00046\",\"G00047\",\"G00048\",\"C02658\",\"C05848\",\"C02273\",\"G00040\",\"G00042\",\"C05852\"," +
		"\"G00039\",\"G00036\",\"C01190\",\"G00037\",\"G00034\",\"C02666\",\"G00035\",\"G00033\",\"G00030\",\"G00031\",\"C09244\",\"C01197\",\"C01194\"," +
		"\"G00029\",\"C02670\",\"G00025\",\"G00026\",\"G00027\",\"G00028\",\"G00021\",\"G00022\",\"G00023\",\"G00024\",\"G00020\",\"C00141\",\"G10841\"," +
		"\"G00018\",\"G00019\",\"C00144\",\"C04121\",\"C00143\",\"C09699\",\"C02220\",\"C02222\",\"G00013\",\"C00148\",\"G00012\",\"C00149\",\"G00011\"," +
		"\"C00147\",\"G00017\",\"G00016\",\"G00015\",\"G00014\",\"G00007\",\"G00008\",\"G00009\",\"C00152\",\"C00151\",\"C00154\",\"C04133\",\"C00153\"," +
		"\"C00156\",\"C00155\",\"C02232\",\"C00157\",\"C00158\",\"C00159\",\"G00002\",\"C03758\",\"G00001\",\"G00004\",\"G00003\",\"G00006\",\"G00005\"," +
		"\"C02614\",\"C00167\",\"C00166\",\"C00164\",\"C00163\",\"C00160\",\"C03741\",\"C06394\",\"C06399\",\"C05894\",\"C05893\",\"C06397\",\"C05892\"," +
		"\"C06398\",\"C05896\",\"C05895\",\"C00168\",\"C00169\",\"C00178\",\"C00177\",\"C06387\",\"C00170\",\"C02637\",\"C00179\",\"C01500\",\"C04314\"," +
		"\"C05904\",\"C05903\",\"C03263\",\"C00259\",\"C06755\",\"C06756\",\"C00258\",\"C14812\",\"C00257\",\"C06753\",\"C11899\",\"C06754\",\"C11897\"," +
		"\"C06752\",\"C14813\",\"C11894\",\"C11895\",\"C14814\",\"C03657\",\"C00250\",\"C16365\",\"C00251\",\"C00254\",\"C00255\",\"C06758\",\"C06757\"," +
		"\"C00253\",\"C00643\",\"C00641\",\"C00647\",\"C04317\",\"C00645\",\"C04302\",\"C00246\",\"C00245\",\"C00249\",\"C00242\",\"C06749\",\"C04308\"," +
		"\"C00655\",\"C04332\",\"C03287\",\"C04330\",\"C06329\",\"C11874\",\"C03677\",\"C06730\",\"C00239\",\"C11878\",\"C00236\",\"C00235\",\"C06731\"," +
		"\"C03283\",\"C00234\",\"C00232\",\"C00233\",\"C00230\",\"C00231\",\"C00627\",\"C03676\",\"C00628\",\"C06322\",\"C06321\",\"C06320\",\"C00624\"," +
		"\"C00621\",\"C11472\",\"C00227\",\"C00224\",\"C06720\",\"C00223\",\"C00221\",\"C03680\",\"C06727\",\"C00222\",\"C06729\",\"C00639\",\"C06728\"," +
		"\"C03684\",\"C00634\",\"C04327\",\"C00636\",\"C00637\",\"C00630\",\"C00631\",\"C00632\",\"C00633\",\"C06712\",\"C11857\",\"C00214\",\"C06711\"," +
		"\"C00212\",\"C16327\",\"C00213\",\"C16328\",\"C00219\",\"C00217\",\"C05947\",\"C06308\",\"C05946\",\"C16737\",\"C00603\",\"C00601\",\"C02591\"," +
		"\"C00606\",\"C02593\",\"C03692\",\"C06714\",\"C16338\",\"C16339\",\"C00203\",\"C00204\",\"C00206\",\"C00207\",\"C11863\",\"C00208\",\"C00209\"," +
		"\"C05936\",\"C11453\",\"C06319\",\"C04185\",\"C12989\",\"C12988\",\"C12987\",\"C12986\",\"C11455\",\"C16331\",\"C00617\",\"C16330\",\"C16332\"," +
		"\"C00618\",\"C16335\",\"C04188\",\"C16334\",\"C16336\",\"C11831\",\"C13309\",\"C05923\",\"C05921\",\"C05922\",\"C09629\",\"C12248\",\"C09621\"," +
		"\"C16348\",\"C02575\",\"C11434\",\"C11435\",\"C11436\",\"C11437\",\"C16357\",\"C16356\",\"C02565\",\"C16358\",\"C16353\",\"C16352\",\"C04785\"," +
		"\"C04780\",\"C04783\",\"C11426\",\"C11425\",\"C11427\",\"C11422\",\"C01494\",\"C05275\",\"C05276\",\"C05273\",\"C05274\",\"C05271\",\"C05272\"," +
		"\"C05270\",\"C03205\",\"C15667\",\"C03203\",\"C03201\",\"C05297\",\"C13747\",\"C05290\",\"C05295\",\"C05294\",\"C15650\",\"C15651\",\"C16317\"," +
		"\"C01477\",\"C00688\",\"C03227\",\"C06793\",\"C06790\",\"C06792\",\"C06791\",\"C00680\",\"C04352\",\"C00681\",\"C03221\",\"C00682\",\"C00683\"," +
		"\"C00697\",\"C00696\",\"C01902\",\"C04751\",\"C04752\",\"C07836\",\"C01909\",\"C07838\",\"C06789\",\"C04756\",\"C00691\",\"C00695\",\"C00692\"," +
		"\"C00668\",\"C00669\",\"C00664\",\"C00666\",\"C03242\",\"C04376\",\"C04377\",\"C03239\",\"C14827\",\"C14826\",\"C06762\",\"C14825\",\"C04778\"," +
		"\"C06760\",\"C14823\",\"C14822\",\"C15699\",\"C03231\",\"C00671\",\"C03232\",\"C03233\",\"C01419\",\"C01416\",\"C05316\",\"C04717\",\"C01414\"," +
		"\"C01829\",\"C01825\",\"C01001\",\"C16243\",\"C08061\",\"C16242\",\"C08062\",\"C16244\",\"C04702\",\"C04706\",\"C05322\",\"C01005\",\"C16276\"," +
		"\"C01010\",\"C01011\",\"C01013\",\"C04738\",\"C04734\",\"C04732\",\"C05337\",\"C01019\",\"C01802\",\"C16264\",\"C01024\",\"C04729\",\"C16263\"," +
		"\"C04722\",\"C16262\",\"C08491\",\"C05345\",\"C01026\",\"C07335\",\"C01455\",\"C02167\",\"C01035\",\"C02166\",\"C01036\",\"C02165\",\"C16210\"," +
		"\"C01037\",\"C01454\",\"C05356\",\"C01468\",\"C16203\",\"C16204\",\"C01043\",\"C01040\",\"C01041\",\"C15606\",\"C02985\",\"C01852\",\"C01851\"," +
		"\"C02987\",\"C07394\",\"C01433\",\"C09107\",\"C01438\",\"C01050\",\"C01051\",\"C02185\",\"C01054\",\"C16237\",\"C16238\",\"C02189\",\"C16236\"," +
		"\"C15613\",\"C16239\",\"C08060\",\"C05379\",\"C05378\",\"C01060\",\"C01061\",\"C02191\",\"C02198\",\"C01063\",\"C01832\",\"C05382\",\"G12625\"," +
		"\"G12626\",\"C05385\",\"C01079\",\"C02124\",\"C05977\",\"C01077\",\"C01074\",\"C00979\",\"G00159\",\"C00978\",\"G00157\",\"G00158\",\"G00155\"," +
		"\"C00974\",\"G00156\",\"G00154\",\"C10860\",\"C02949\",\"C05980\",\"C02946\",\"C04272\",\"C02139\",\"C01089\",\"C05989\",\"C01081\",\"G00146\"," +
		"\"C01080\",\"G00147\",\"C00966\",\"G00148\",\"G00149\",\"G00143\",\"G00144\",\"G00145\",\"C02939\",\"C04287\",\"C05952\",\"C05953\",\"C04281\"," +
		"\"C01885\",\"C05951\",\"C02934\",\"C02140\",\"C02141\",\"G00170\",\"C00957\",\"C05958\",\"C00958\",\"C05957\",\"C05956\",\"G00171\",\"C05955\"," +
		"\"C05954\",\"C00951\",\"C01094\",\"C02526\",\"C02527\",\"C00956\",\"C01092\",\"C02528\",\"C00954\",\"C05961\",\"C05962\",\"C04294\",\"C05963\"," +
		"\"C05964\",\"C12204\",\"C12203\",\"C02923\",\"G00160\",\"C05966\",\"C05965\",\"G00162\",\"G00163\",\"G00164\",\"G00169\",\"C00944\",\"C00280\"," +
		"\"C00286\",\"G00119\",\"C00283\",\"G00117\",\"C02918\",\"G00118\",\"G00116\",\"G00115\",\"C02504\",\"G00114\",\"G00113\",\"C02501\",\"G00112\"," +
		"\"C11901\",\"C11900\",\"G00111\",\"G00110\",\"C17346\",\"C17345\",\"C11907\",\"C17343\",\"C17339\",\"C00299\",\"C04236\",\"G00108\",\"C00295\"," +
		"\"G00109\",\"C00294\",\"C02515\",\"C02514\",\"C03618\",\"C02512\",\"C02105\",\"C02106\",\"C00262\",\"C04246\",\"C06250\",\"C06251\",\"C00266\"," +
		"\"C00263\",\"C04244\",\"C03648\",\"C00269\",\"C00267\",\"G00132\",\"C00268\",\"G00131\",\"G00130\",\"G00128\",\"C00270\",\"G00129\",\"C06241\"," +
		"\"C04257\",\"C00272\",\"C00275\",\"C05300\",\"G00121\",\"C00279\",\"G00120\",\"G00123\",\"G00122\",\"G00125\",\"G00124\",\"C05306\",\"G00127\"," +
		"\"C05307\",\"G00126\",\"C00988\",\"C02110\",\"C03594\",\"C00909\",\"C00906\",\"C00905\",\"C00903\",\"C03972\",\"C04652\",\"C04640\",\"C00916\"," +
		"\"C03190\",\"C04637\",\"C04635\",\"C04633\",\"C04631\",\"C00925\",\"C14152\",\"C07715\",\"C14151\",\"C00921\",\"C03586\",\"C13433\",\"C04623\"," +
		"\"C04620\",\"C00935\",\"C14144\",\"C07729\",\"C14143\",\"C14145\",\"C03175\",\"C03170\",\"C00931\",\"C03589\",\"C06202\",\"C06204\",\"C06203\"," +
		"\"C03939\",\"C06615\",\"C06614\",\"C03557\",\"C06612\",\"C03160\",\"C06613\",\"C04691\",\"C06610\",\"C06611\",\"C03164\",\"C03167\",\"C06210\"," +
		"\"C06609\",\"C03569\",\"C06600\",\"C04688\",\"C12833\",\"C12834\",\"C12835\",\"C12836\",\"C12831\",\"C12832\",\"C06638\",\"C06637\",\"C06636\"," +
		"\"C03539\",\"C04677\",\"C04672\",\"C03968\",\"C06231\",\"C03546\",\"C04666\",\"C00309\",\"C00307\",\"C00300\",\"C00310\",\"C03112\",\"G01813\"," +
		"\"C03114\",\"C06640\",\"C03906\",\"C00319\",\"C00315\",\"C00314\",\"C00313\",\"C00312\",\"C00311\",\"C06671\",\"C06670\",\"C06677\",\"C15980\"," +
		"\"C00323\",\"C00322\",\"C00325\",\"C03912\",\"C00327\",\"C00328\",\"C00331\",\"C00332\",\"C00330\",\"C15975\",\"C15977\",\"C15979\",\"C15970\"," +
		"\"C00334\",\"C00333\",\"C03920\",\"C00338\",\"C00337\",\"C15926\",\"C00341\",\"C15545\",\"C07731\",\"C07734\",\"C00344\",\"C00345\",\"C07732\"," +
		"\"C00346\",\"C15547\",\"C07733\",\"C00354\",\"C00353\",\"C00352\",\"C00350\",\"C00355\",\"C00356\",\"C07216\",\"C07215\",\"C07214\",\"C07213\"," +
		"\"C07212\",\"C07211\",\"C00361\",\"C00360\",\"C00363\",\"C00362\",\"C00365\",\"C00364\",\"C00366\",\"C00369\",\"C01380\",\"C00371\",\"C00376\"," +
		"\"C00379\",\"C00378\",\"C00399\",\"C01724\",\"C00394\",\"C00395\",\"C17401\",\"C00398\",\"C01312\",\"C00390\",\"C01717\",\"G10694\",\"C00389\"," +
		"\"C00388\",\"C00380\",\"C00381\",\"C00386\",\"C00387\",\"C00385\",\"C08830\",\"C05479\",\"C07304\",\"C05484\",\"C05488\",\"C01732\",\"C05480\"," +
		"\"C01346\",\"C05675\",\"C05676\",\"C05450\",\"C05672\",\"C05673\",\"C05455\",\"C05451\",\"C05452\",\"C02035\",\"C05453\",\"C05454\",\"C09421\"," +
		"\"C05460\",\"C05688\",\"C05681\",\"C05467\",\"C02814\",\"C11811\",\"C05430\",\"C05431\",\"C05432\",\"C11816\",\"C05433\",\"C05435\",\"C05437\"," +
		"\"C05439\",\"C11821\",\"C05443\",\"C01300\",\"C05440\",\"C02004\",\"C05441\",\"C05446\",\"C05447\",\"C05444\",\"C05445\",\"C05449\",\"C05448\"," +
		"\"C01304\",\"C01302\",\"C01301\",\"C02462\",\"C02463\",\"C05635\",\"C02465\",\"C00877\",\"C00876\",\"C05414\",\"C05413\",\"C05634\",\"C02477\"," +
		"\"C01794\",\"C00862\",\"C00864\",\"C03516\",\"G10611\",\"C05427\",\"C05422\",\"C00860\",\"C02061\",\"C06124\",\"C06125\",\"C03506\",\"C00894\"," +
		"\"C02059\",\"C06157\",\"C06156\",\"C05668\",\"C00885\",\"C02046\",\"C00882\",\"C05662\",\"C06148\",\"C00836\",\"C01762\",\"C06179\",\"C06178\"," +
		"\"C06174\",\"C02880\",\"C13453\",\"C06171\",\"C13455\",\"C00822\",\"C00826\",\"C00827\",\"C05608\",\"C00828\",\"C00829\",\"C04619\",\"C04618\"," +
		"\"C06167\",\"C06163\",\"C06165\",\"C06161\",\"C06160\",\"C03090\",\"C00857\",\"C02480\",\"C01780\",\"C05619\",\"C02483\",\"C00859\",\"C01789\"," +
		"\"C02094\",\"C05610\",\"C02090\",\"C00846\",\"C00842\",\"C05625\",\"C00847\",\"C02083\",\"C05623\",\"C06181\",\"C06183\",\"C06182\",\"C08814\"," +
		"\"C03454\",\"C03453\",\"C10700\",\"C00800\",\"C00805\",\"C03069\",\"C03460\",\"C00811\",\"C04501\",\"C00817\",\"C00819\",\"C00818\",\"C11691\"," +
		"\"C04536\",\"C03470\",\"C03479\",\"C03082\",\"C11680\",\"C03089\",\"C04525\",\"C09024\",\"C03862\",\"C00407\",\"C00408\",\"C00010\",\"C00011\"," +
		"\"C06104\",\"C03838\",\"C03028\",\"C06102\",\"C00406\",\"C04556\",\"C04554\",\"C03021\",\"C00016\",\"C00015\",\"C00014\",\"C06510\",\"C00019\"," +
		"\"C00018\",\"C06509\",\"C00418\",\"C06508\",\"C06505\",\"C06504\",\"C00001\",\"C06507\",\"C00002\",\"C06506\",\"C00410\",\"C00411\",\"C06112\"," +
		"\"C03845\",\"C00415\",\"C00416\",\"C04549\",\"C00417\",\"C03012\",\"C00004\",\"C00003\",\"C00006\",\"C06503\",\"C00005\",\"C03428\",\"C00008\"," +
		"\"C00007\",\"C00009\",\"C03434\",\"C09820\",\"C09821\",\"C13508\",\"C11393\",\"C03820\",\"C02305\",\"C03824\",\"C09819\",\"C03033\",\"C09816\"," +
		"\"C09818\",\"C09817\",\"C06555\",\"C06552\",\"C06551\",\"C06554\",\"C06553\",\"C04598\",\"C07130\",\"C00440\",\"C04596\",\"C00441\",\"C00446\"," +
		"\"C00445\",\"C00448\",\"C00447\",\"C00449\",\"C14610\",\"C06547\",\"C00450\",\"C04582\",\"C01290\",\"C00459\",\"C00458\",\"C06548\",\"C00426\"," +
		"\"C00427\",\"C00422\",\"C00423\",\"C00429\",\"C01284\",\"C16519\",\"C01279\",\"C00430\",\"C00439\",\"C00438\",\"C00437\",\"C00436\",\"C01278\"," +
		"\"C01277\",\"C01274\",\"C01273\",\"C01272\",\"C06593\",\"C00482\",\"C06594\",\"C06599\",\"C01269\",\"C01268\",\"C06597\",\"C01262\",\"C01264\"," +
		"\"C16524\",\"C01267\",\"C03492\",\"C00493\",\"C07100\",\"C15810\",\"C06588\",\"C15813\",\"C06589\",\"C07102\",\"C15814\",\"C07101\",\"C15815\"," +
		"\"C15816\",\"C07103\",\"C01251\",\"C01252\",\"C01250\",\"C00498\",\"C00499\",\"C00463\",\"C07111\",\"C00460\",\"C15808\",\"C15805\",\"C15806\"," +
		"\"C15803\",\"C15804\",\"C15801\",\"C01246\",\"C15802\",\"C15800\",\"C01242\",\"C01243\",\"C01245\",\"C16540\",\"C16541\",\"C01241\",\"C00469\"," +
		"\"C11641\",\"C00468\",\"C00475\",\"C00474\",\"C00473\",\"C06561\",\"C00470\",\"C06564\",\"C07123\",\"C06565\",\"C01236\",\"C11638\",\"C05594\"," +
		"\"C01212\",\"C01218\",\"C01213\",\"C04916\",\"C04919\",\"C01222\",\"C01220\",\"C01617\",\"C01226\",\"C01227\",\"C17622\",\"C17621\",\"C01606\"," +
		"\"C06098\",\"C06099\",\"C07209\",\"C07208\",\"C04932\",\"C06482\",\"C06481\",\"C06089\",\"C06087\",\"C06082\",\"C01204\",\"C06090\",\"C01209\"," +
		"\"C05552\",\"C05556\",\"C05557\",\"C18151\",\"C02700\",\"G10595\",\"G10598\",\"G10599\",\"G10596\",\"G10597\",\"C11700\",\"C02714\",\"C05576\"," +
		"\"C05577\",\"C05578\",\"C05579\",\"C00792\",\"C00791\",\"C00794\",\"C12448\",\"C00798\",\"C05589\",\"C15882\",\"C05587\",\"C05588\",\"C00783\"," +
		"\"C05585\",\"C15883\",\"C05583\",\"C05584\",\"C00780\",\"C05581\",\"C05582\",\"C05580\",\"C00785\",\"C12455\",\"C00788\",\"C00777\",\"C00049\"," +
		"\"C00047\",\"C00048\",\"C05519\",\"C05123\",\"C05778\",\"C05122\",\"C05125\",\"C05773\",\"C05772\",\"C06037\",\"C02325\",\"C05775\",\"C00046\"," +
		"\"C00044\",\"C00043\",\"C02741\",\"C00042\",\"C00041\",\"C05512\",\"C05527\",\"C00058\",\"C00763\",\"C00762\",\"C00059\",\"C01693\",\"C01694\"," +
		"\"C02330\",\"C06024\",\"C06025\",\"C06026\",\"C02336\",\"C00055\",\"C08276\",\"C00051\",\"C00052\",\"C06022\",\"C03406\",\"C05141\",\"C02723\"," +
		"\"C00025\",\"C00751\",\"C00026\",\"C00029\",\"C05758\",\"C05145\",\"C05759\",\"C05756\",\"C05757\",\"C05755\",\"C05754\",\"C05753\",\"C05752\"," +
		"\"C05751\",\"C05750\",\"C00020\",\"C11354\",\"C06010\",\"C04006\",\"C00024\",\"C00750\",\"C00022\",\"C00021\",\"C00036\",\"C00037\",\"C00039\"," +
		"\"C00745\",\"C02737\",\"C02352\",\"C00748\",\"C02350\",\"C05138\",\"C02739\",\"C01672\",\"C01673\",\"C02359\",\"C05764\",\"C05763\",\"C06006\"," +
		"\"C01678\",\"C06007\",\"C05760\",\"C06001\",\"C06002\",\"C05762\",\"C05761\",\"C00031\",\"C06000\",\"C00033\",\"C00032\",\"C02730\",\"C00035\"," +
		"\"C00034\",\"C02364\",\"C17542\",\"C17541\",\"C00735\",\"C00736\",\"C00089\",\"C06071\",\"C06070\",\"C00085\",\"C06076\",\"C00086\",\"C00083\"," +
		"\"C00084\",\"C00082\",\"C00080\",\"C05730\",\"C05746\",\"C02378\",\"C05745\",\"C05748\",\"C02375\",\"C05747\",\"C00726\",\"C05749\",\"C02371\"," +
		"\"C00729\",\"C02370\",\"C00725\",\"C11348\",\"C00721\",\"C02798\",\"C00099\",\"C00094\",\"C00095\",\"C00096\",\"C00097\",\"C00090\",\"C00091\"," +
		"\"C00092\",\"C00093\",\"C06069\",\"C05744\",\"C00718\",\"C17562\",\"C05103\",\"C00719\",\"C05100\",\"C05712\",\"C17560\",\"C00714\",\"C17561\"," +
		"\"C00063\",\"C06055\",\"C06054\",\"C00064\",\"C00061\",\"C00062\",\"C00067\",\"C00068\",\"C04043\",\"C04046\",\"C00065\",\"C09390\",\"C00060\"," +
		"\"C05107\",\"C05108\",\"C17551\",\"C00705\",\"C17552\",\"C17554\",\"C03892\",\"C00072\",\"C05500\",\"C00073\",\"C06041\",\"C05501\",\"C00074\"," +
		"\"C05502\",\"C00075\",\"C00077\",\"C06040\",\"C00078\",\"C00079\",\"C17559\"";

	private GlammSession sm;
	
	public CompoundGlammDAOImpl(final GlammSession sm) {
		this.sm = sm;
	}
	
	@Override
	public Compound getCompound(String id, String dbName) {

		Compound cpd = null;
		String sql 	=	"select C.guid, C.commonName, C.mass, C.formula, C.smiles, C.inchi " + 
		"from glamm.GlammCompound C " +
		"join glamm.GlammXref X on (X.fromGuid=C.guid) " +
		"join glamm.GlammEntity2DataSource E2DS on (E2DS.entityGuid=C.guid) " +
		"join glamm.GlammDataSource DS on (E2DS.dataSourceGuid=DS.guid) " +
		"where X.toXrefId=? and DS.dbName=?;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, id);
			ps.setString(2, dbName);

			ResultSet rs = ps.executeQuery();

			if(rs.next()) {

				cpd = new Compound();

				cpd.setName(rs.getString("commonName"));
				cpd.setMass(rs.getString("mass"));
				cpd.setFormula(rs.getString("formula"));
				cpd.setSmiles(rs.getString("smiles"));
				cpd.setInchi(rs.getString("inchi"));
				cpd.addXref(id, dbName);

			}



			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return cpd;
	}
	
	@Override
	public List<Compound> getCompoundsForSearch(Set<String> dbNames) {
		
		List<Compound> cpds = null;
		String dbNamesString = GlammUtils.joinCollection(dbNames);
		
		String sql = "select X.toXrefId, X.xrefDbName, C.commonName as synonym " +
		"from glamm.GlammCompound C " +
		"join glamm.GlammXref X on (C.guid=X.fromGuid) " +
		"where X.xrefDbName in (" + dbNamesString + ") " +
		"and X.toXrefId in (" + GLOBAL_MAP_CPD_IDS + ") " +
		"union " +
		"select X.toXrefId, X.xrefDbName, S.synonym " +
		"from glamm.GlammXref X " +
		"join glamm.GlammSynonym S on (X.fromGuid=S.forGuid) " +
		"where X.xrefDbName in (" + dbNamesString + ") " +
		"and S.synonym not like \"%<%\" " +
		"and S.synonym not like \"%>%\" " +
		"and S.synonym not like \"%&%\" " +
		"and X.toXrefId in (" + GLOBAL_MAP_CPD_IDS + ");";
		
		try {
			
			Connection connection = GlammDbConnectionPool.getConnection(sm);
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(sql);

			while(rs.next()) {

				Compound cpd = new Compound();

				cpd.setName(rs.getString("synonym"));
				cpd.addXref(rs.getString("toXrefId"), rs.getString("xrefDbName"));
				
				if(cpds == null)
					cpds = new ArrayList<Compound>();
				cpds.add(cpd);

			}



			rs.close();
			connection.close();
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return cpds;
	}

}