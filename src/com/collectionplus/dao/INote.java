package com.collectionplus.dao;

import java.util.List;
import java.util.Map;

import com.collectionplus.bean.Note;

public interface INote {
	
	public List<Note> findNotesByLinkId(Integer linkID);
	public void modifyNote(Map<String,String> map); 
	public void deleteNote(Integer noteID);
	public void createNote(Map<String,String> map);
}
