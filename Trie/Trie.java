/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw1;

/**
 *
 * @author USER
 */
public class Trie {
    public TrieNode root;
    public static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    
    public Trie(String[] args){
        boolean result = false;
        root = new TrieNode((char)0);
        
        for (int i = 0; i < args.length; i++){ 

            if(args[i]==null){break;}
            result = add(args[i]);
          
        }
        
    }
    
    private int GetSize(TrieNode root){
		int wordscounted = 0;

		if(root.isTerminal == true){wordscounted++;}

		for(int k=0; k<alphabet.length(); k++){
			if(root.children[k] != null){
                            wordscounted = wordscounted + GetSize(root.children[k]);
			}
		}
		return wordscounted;

	}
    
        private String[] GetPrefix(TrieNode root, String wordarray, String[] words){
            String word_string;
           
            if(root == null)
                return (words);
   
            if(root.isTerminal ){  
                
                for(int i=0; i<words.length; i++){
                    if(words[i]==null){words[i] = wordarray; break;}
                }
            }
            for(int i=0; i<alphabet.length(); i++)
            {
                if(root.children[i] != null){                    
                    GetPrefix(root.children[i], wordarray + root.children[i].c,words);  
                }
             }
            return(words);
        }

	public boolean add(String word){
		int letter;
		TrieNode enterNode = root;

		for(int j=0; j<word.length(); j++){
			letter = word.charAt(j) - 'a';
			if(enterNode.children[letter] == null){
				enterNode.children[letter] = new TrieNode(word.charAt(j));
			}
			enterNode = enterNode.children[letter];
		}
		enterNode.isTerminal = true;
                return(contains(word));

	}

	public boolean contains(String word){
		int letter; 
		TrieNode checkNode = root;
		
		for(int j=0; j<word.length(); j++){
			letter = word.charAt(j) - 'a';
			if(checkNode.children[letter] == null){
				return false;
			}
			checkNode = checkNode.children[letter];
		}

		if(checkNode.isTerminal){return true;}
		else{return false;}
	}

	public int size(){
            TrieNode checkNode = root;
            int totalwords = 0;
		
            totalwords = GetSize(checkNode);
            return totalwords;	
	}
        
        public String[] findlength(TrieNode root, int length, int layer,String[] words, String word, char[] cmp,String wordtocheck, int max, int[]count){
            
            if(root.isTerminal && layer == length){
                for(int k=0; k<wordtocheck.length(); k++){
                    cmp[0]=wordtocheck.charAt(k);
                    cmp[1]=word.charAt(k);
                    
                    if(cmp[0] != cmp[1]){
                        count[0]++;
                    }    
                }
                if(count[0]<=max){
                    for(int i=0; i<words.length; i++){        
                        if(words[i]==null){words[i] = word; break;}
                    }
                }
                count[0]=0;
            }
            
            for(int i=0; i<alphabet.length(); i++)
            {
                if(root.children[i] != null){                    
                    findlength(root.children[i], length, layer+1, words, word+root.children[i].c, cmp, wordtocheck, max, count);  
                }
             }
            return(words);
        }
        
        private int[] countwords(TrieNode root, int length, int layer,int[] count){
           
            if(root.isTerminal && layer == length){
                
                count[0] = count[0] + 1;
               
            }
            
            for(int i=0; i<alphabet.length(); i++)
            {
                if(root.children[i] != null){
                    
                   count = countwords(root.children[i], length, layer+1, count);  
                }
            }
            
            return(count);
        }

	public String[] differByOne(String word){
            String[] words, finalwords;
            String word_check = "";
            int[] arraylen;
            int len;
            TrieNode start = root;
            //int[] count, len;
            int[] count;
            char[] cmp;
            
            count = new int[1];
            cmp = new char[2];
            arraylen = new int[1];
            arraylen = countwords(start, word.length(), 0, arraylen);
            
            len = arraylen[0];
            words = new String[arraylen[0]];
            
            words = findlength(start, word.length(), 0, words, word_check, cmp, word, 1, count);
                     
            return(words);
        }

	public String[] differBy(String word, int max){
            String[] words, finalwords;
            String word_check = "";
            int[] arraylen;
            TrieNode start = root;           
            int len;
            int[] count;
            char[] cmp;
            
            count = new int[1];
            cmp = new char[2];
            arraylen = new int[1];
            arraylen = countwords(start, word.length(), 0, arraylen);
           
            len = arraylen[0];
            words = new String[arraylen[0]];
            
            words = findlength(start, word.length(), 0, words, word_check,cmp, word, max, count);

            return(words);
        }

	public String[] wordsOfPrefix(String prefix){
            TrieNode newletters = root;
            int letter, subtrie, count=0;
            
            String[] words;
        
            for(int k=0; k<prefix.length(); k++){
                
                letter = prefix.charAt(k) - 'a';
                if(newletters.children[letter] == null){
                  
                    return null;
                }
                newletters = newletters.children[letter]; 
            }//reached end of prefix
            
            subtrie = GetSize(newletters);
            words = new String[subtrie];
            for(int k=0; k<subtrie; k++){words[k] = null;}

            return (GetPrefix(newletters, prefix,words));
        }
        
        private String[] toStrRecursive(TrieNode root, String[] preorder){
            int k;
            for(k=0; k<alphabet.length(); k++){
                if(root.children[k]!=null){
                    break;
                }             
            }
            if(k==alphabet.length()|| root.isTerminal){preorder[0] = preorder[0] +'!';}
            for(int i=0; i<alphabet.length(); i++)
            {
                if(root.children[i] != null){
                   preorder[0] = preorder[0] + " "+ root.children[i].c;
                   toStrRecursive(root.children[i], preorder);   
                }
             }   
            return(preorder);
        }
        public String toString(){
            TrieNode start= root;
            String[] preorder;
            
            preorder = new String[1];
            preorder[0] = "";
            preorder = toStrRecursive(start, preorder);
            //preorder[0]=preorder[0]+'\n';
            return(preorder[0]);
        }
        
        private String recurdot(String dot, TrieNode start, int hash){
            char c='"';
            
            if(start==root){ 
                dot = dot+'\n'+'\t'+root.hashCode()+" ";
                dot = dot+"[label="+c+"ROOT"+c+" ,shape=circle, color=black]";
                //dot = dot+'\n'+'\t'+root.hashCode()+" -- ";
                    
            }
            else if(start.isTerminal){
                dot = dot+'\n'+'\t'+hash+" -- "+start.hashCode();
                dot = dot+'\n'+'\t'+start.hashCode()+" [label="+c+start.c+c+" ,shape=circle, color=red]";
                
            }
            else if(start.isTerminal == false){
                dot = dot+'\n'+'\t'+hash+" -- "+start.hashCode();
                dot = dot+'\n'+'\t'+start.hashCode()+" [label="+c+start.c+c+" ,shape=circle, color=black]";
                
            }
            for(int i=0; i<alphabet.length(); i++)
            {
                if(start.children[i] != null){
                    dot = recurdot(dot, start.children[i], start.hashCode());

                }
             }
            return(dot);
        }
        
        public String toDotString(){
            int k;
            char c='"';
            String final_dot = "graph Trie {";

            final_dot = recurdot(final_dot, root,root.hashCode());

            final_dot = final_dot+'\n'+"}";
            
            return(final_dot);
        }
}
