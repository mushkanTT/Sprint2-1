package com.cg.bugtracking.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.bugtracking.entity.Bug;
import com.cg.bugtracking.exceptions.OperationFailedException;
import com.cg.bugtracking.exceptions.ResourceNotFoundException;
import com.cg.bugtracking.repository.BugRepository;

import static com.cg.bugtracking.util.AppConstant.OPERATION_FAILED_CONST;
import static com.cg.bugtracking.util.AppConstant.BUG_NOT_FOUND_CONST;

@Service
public class BugServiceImpl implements BugService {

	@Autowired
	private BugRepository bugRepository;

	@Transactional
	@Override
	public Bug createBug(Bug bug) {
		Bug bugObj = null;
		try {
			bugObj = bugRepository.save(bug);

		} catch (Exception e) {
			throw new OperationFailedException(OPERATION_FAILED_CONST + e.getMessage());
		}
		return bugObj;
	}

	@Transactional
	@Override
	public Bug updateBug(long id, Bug bug) {
		Optional<Bug> bugObj = null;
		Bug updatedBug = null;
		bugObj = bugRepository.findById(id);
		if (!bugObj.isPresent()) {
			throw new ResourceNotFoundException(BUG_NOT_FOUND_CONST + bug.getBugId());
		} else {
			
			bugObj.get().setStatus(bug.getStatus());
			bugObj.get().setPriority(bug.getPriority());
			bugObj.get().setAssignee(bug.getAssignee());
			bugObj.get().setBugDesc(bug.getBugDesc());
			bugObj.get().setEndDate(bug.getEndDate());
			bugObj.get().setStartDate(bug.getStartDate());
			bugObj.get().setType(bug.getType());
			try {
				updatedBug = bugRepository.save(bugObj.get());
			} catch (Exception e) {
				throw new OperationFailedException(OPERATION_FAILED_CONST + e.getMessage());
			}

		}

		return updatedBug;
	}

	@Override
	public Bug getBug(long id) {

		Optional<Bug> bug = bugRepository.findById(id);
		
		if (!bug.isPresent())
			throw new ResourceNotFoundException(BUG_NOT_FOUND_CONST + id);

		return bug.get();
	}

	@Override
	public Bug deleteBug(long id) {
		Optional<Bug> bugObj = bugRepository.findById(id);
		if (!bugObj.isPresent()) {
			throw new ResourceNotFoundException(BUG_NOT_FOUND_CONST + id);
		} else {
			try {
				bugRepository.delete(bugObj.get());
			}catch(Exception e) {
				throw new OperationFailedException(OPERATION_FAILED_CONST +e.getMessage());
			}			
		}
		
		return bugObj.get();
	}
	
	@Override
	public List<Bug> getAllBugs() {
		return bugRepository.findAll();
	}

	@Override
	public List<Bug> getAllBugsByStatus(String status) {
		return bugRepository.findByStatus(status);
	}



}
