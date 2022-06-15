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
public class TrieNode {
	
	public TrieNode[] children;
	public boolean isTerminal;
        public char c;
        

	public TrieNode(char initc){
                c = initc;
		children = new TrieNode[26];
		for(int i=0; i<26; i++){
			children[i] = null;
		}
                isTerminal = false;
	}	
}
